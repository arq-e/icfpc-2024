#include <bits/stdc++.h>
#define ll long long

using namespace std;

const int MAX_STEPS = 1000000; //TODO: ignore when compressing
const int LONG_STRING_MIN_LEN = 70;
const bool USE_COMPRESSION = true;
const vector<int> moves = {1, 0, -1, 0, 1};
const vector<char> dirs = {'D', 'L', 'U', 'R'};

// const vector<char> converted_dirs = {'>', 'F', 'O', 'L'};
unordered_map<char, char> converted_dirs = {
  {'D', '>'}, {'L', 'F'}, {'U', 'O'}, {'R', 'L'}
};

struct Pos {
  int x;
  int y;
  string path;
  Pos(int x, int y, string path) : x(x), y(y), path(path) {}
};

struct LambdaMan {
  int x = 0;
  int y = 0;
  string output;
  vector<vector<char>> grid;
  ifstream &in;
  bool has_dead_ends = true;
  size_t total_dots = 0;

  LambdaMan(ifstream &ifs) : in(ifs) {
    string s;
    while (in >> s) {
      grid.push_back(vector<char>(s.begin(), s.end()));
    }
    set_start_pos();
    count_total_dots();
  }

  void set_start_pos() {
    for (int i = 0; i < grid.size(); ++i) {
      for (int j = 0; j < grid[i].size(); ++j) {
        if (grid[i][j] == 'L') {
          x = i;
          y = j;
          return;
        }
      }
    }
  }

  void count_total_dots() {
    for (int i = 0; i < grid.size(); ++i) {
      for (int j = 0; j < grid[i].size(); ++j) {
        if (grid[i][j] == '.') {
          ++total_dots;
        }
      }
    }
  }

  void solve_by_order() {
    bool changed = true;
    while (changed) {
      changed = false;
      for (int i = 0; i < grid.size(); ++i) {
        for (int j = 0; j < grid[i].size(); ++j) {
          if (grid[i][j] == '.') {
            string path = build_shortest_path(i, j);
            cout << path << endl;
            walk(path);
            output += path;
            changed = true;
          }
        }
      }
    }
  }

  void solve_by_closest_dot_in_direction(char start_dir) {
    while(true) {
      string path;
      int next_x = x;
      int next_y = y;
      switch(start_dir) {
        case 'L': next_y -= 1; break;
        case 'R': next_y += 1; break;
        case 'U': next_x -= 1; break;
        case 'D': next_x += 1; break;
        default: break;
      }
      if (0 <= next_x && next_x < grid.size() && 0 <= next_y && next_y < grid[0].size() && grid[next_x][next_y] == '.') {
        path.push_back(start_dir);
        walk(path);
        output += path;
      } else {
        path = build_shortest_path_to_dot();
        if (path.size() == 0) {
          break;
        }
        walk(path);
        output += path;
        start_dir = path.back();
      }
    };
  }

  void solve_by_closest_dot() {
    string path;
    do {
      path = build_shortest_path_to_dot();
      // cout << path << endl;
      walk(path);
      output += path;
    } while (path != "");
  }

  void solve_by_closest_dead_end() {
    string path;
    do {
      path = build_path_to_closest_dead_end();
      walk(path);
      output += path;
    } while (path != "");
  }


  static string find_best_compression(int n, char dir) {
    int size = n;
    string header;
    for (int i = 4; i <= n / 2; ++i) {
      if (n % (2 * i) != 0) continue;

      string tmp;
      int steps = n / (2 * i);
      for (int j = 0; j <= steps; ++j) {
        tmp += "B$ L# B. v# v# ";
      }
      tmp += "S";
      tmp.append(i, converted_dirs[dir]);
      tmp += " v!";
      // cout << n << " " << tmp.size() << " " << tmp << endl;
      if (tmp.size() < size) {
        size = tmp.size();
        header = tmp;
      }
    }
    return header;
  }

  static string convert(string path) {
    string res;
    for (char c: path) {
      res.push_back(converted_dirs[c]);
    }
    return res;
  }

  static string compress(string path) {
    int l = 0;
    int r = 0;
    string tmp = "";
    string cur = "";
    bool used = false;
    for (r = 1; r < path.size(); ++r) {
      if (path[r] != path[l]) {
        if (r-l >= LONG_STRING_MIN_LEN) {
          auto comp = find_best_compression(r - l, path[l]);
          if (!comp.empty()) {
            used = true;
            if (cur.size()) {
              tmp += "B. S" + convert(cur) + " ";
            }
            tmp += comp;
          } else {
            cur += path.substr(l, r - l);
          }
        } else {
          cur += path.substr(l, r - l);
        }
        l = r;
      }
    }

    cur += path.substr(l, r - l - 1);
    if (cur.size() && used) {
      tmp = "B. " + tmp + " S";
      tmp += convert(cur);
    }
    return tmp;
  }

  string solve() {
    solve_by_closest_dot_in_direction('D');
    if (USE_COMPRESSION) {
        string compressed = compress(output);
        if (0 < compressed.size() && compressed.size() < output.size()) {
          output = compressed;
        }
    } 
    return output;
  }

  void walk(string path) {
    for (char c : path) {
      switch (c) {
        case 'R': ++y; break;
        case 'L': --y; break;
        case 'U': --x; break;
        case 'D': ++x; break;
      }
      grid[x][y] = ' ';
    }
  }

  string build_shortest_path(int tx, int ty) {
    queue<Pos> q;
    q.push({x, y, ""});
    set<pair<int, int>> visited;
    visited.insert({x, y});
    while (!q.empty()) {
      Pos p = q.front();
      q.pop();
      if (p.x == tx && p.y == ty) {
        return p.path;
      }
      for (int i = 0; i < 4; ++i) {
        int nx = p.x + moves[i];
        int ny = p.y + moves[i + 1];
        if (nx >= 0 && nx < grid.size() && ny >= 0 && ny < grid[0].size() && grid[nx][ny] != '#' && !visited.contains({nx, ny})) {
          q.push({nx, ny, p.path + dirs[i]});
          visited.insert({nx, ny});
        }
      }
    }
    return "";
  }

  string build_shortest_path_to_dot() {
    queue<Pos> q;
    q.push({x, y, ""});
    set<pair<int, int>> visited;
    visited.insert({x, y});
    while (!q.empty()) {
      Pos p = q.front();
      q.pop();
      if (grid[p.x][p.y] == '.') {
        return p.path;
      }
      for (int i = 0; i < 4; ++i) {
        int nx = p.x + moves[i];
        int ny = p.y + moves[i + 1];
        if (nx >= 0 && nx < grid.size() && ny >= 0 && ny < grid[0].size() && grid[nx][ny] != '#' && !visited.contains({nx, ny})) {
          q.push({nx, ny, p.path + dirs[i]});
          visited.insert({nx, ny});
        }
      }
    }
    return "";
  }

  bool is_dead_end(int tx, int ty) {
    if (grid[tx][ty] != '.') {
      return false; // ignore walls
    }
    int wall_count = 4;
    for (int i = 0; i < 4; ++i) {
      int nx = tx + moves[i];
      int ny = ty + moves[i + 1];
      if (nx >= 0 && nx < grid.size() && ny >= 0 && ny < grid[0].size() && grid[nx][ny] == '.') {
        --wall_count;
      }
    }
    return wall_count >= 3;
  }

  string build_path_to_closest_dead_end() {
    if (has_dead_ends) {
      queue<Pos> q;
      q.push({x, y, ""});
      set<pair<int, int>> visited;
      visited.insert({x, y});
      while (!q.empty()) {
        Pos p = q.front();
        q.pop();
        if (is_dead_end(p.x, p.y)) {
          return p.path;
        }
        for (int i = 0; i < 4; ++i) {
          int nx = p.x + moves[i];
          int ny = p.y + moves[i + 1];
          if (nx >= 0 && nx < grid.size() && ny >= 0 && ny < grid[0].size() && grid[nx][ny] != '#' && !visited.contains({nx, ny})) {
            q.push({nx, ny, p.path + dirs[i]});
            visited.insert({nx, ny});
          }
        }
      }
    }
    has_dead_ends = false;
    return build_shortest_path_to_dot();
  }
};

void solve_all() {
  for (int i = 1; i <= 21; ++i) {
    string input = "../inputs/lambdaman/" + to_string(i);
    string output = "../outputs/lambdaman/" + to_string(i);
    string score_path = "../scores/lambdaman/" + to_string(i);
    ifstream score_ifs(score_path);
    int bestScore = MAX_STEPS+1;
    score_ifs >> bestScore;
    score_ifs.close();

    ifstream ifs(input);
    if (!ifs) {
      cout << "File not found: " << input << endl;
      continue;
    }
    LambdaMan hero(ifs);
    ifs.close();


    string ans = hero.solve();
    int score = ans.size();

    if (0 < score && score < bestScore) {
      cout << "Task " << i << " has new best score: " << score << ", previous best was: " << bestScore << endl;
      bestScore = score;
      ofstream ofs(output);
      ofs << ans;
      ofs.close();

      ofstream score_ofs(score_path);
      score_ofs << bestScore;
      score_ofs.close();
    } else {
      int l = 0;
      int long_strings = 0;
      for (int r = 1; r < score; ++r) {
        if (ans[r] != ans[l]) {
          if (r - l >= LONG_STRING_MIN_LEN) {
            ++long_strings;
          }
          l = r;
        }
      }
      cout << "Task " << i << " " << score << "/" << bestScore << ", total_dots = " << hero.total_dots << 
        ", long_strings = " << long_strings << endl;
    }

  }
}

void test_compress() {
  string test;
  test.push_back('D');
  test.append(80, 'U');
  test.push_back('D');
  cout << test << endl;
  cout << LambdaMan::compress(test) << endl;
}


int main(int argc, char **argv) {
  // ios::sync_with_stdio(false);
  // cin.tie(0);
  // test_compress();
  solve_all();
  return 0;
}