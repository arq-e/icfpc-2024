#include <bits/stdc++.h>
#define ll long long

using namespace std;

const int MAX_STEPS = 1000000; //TODO: ignore when compressing
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

  LambdaMan(ifstream &ifs) : in(ifs) {
    string s;
    while (in >> s) {
      grid.push_back(vector<char>(s.begin(), s.end()));
    }
    set_start_pos();
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

  void solve_by_closest_dot() {
    string path;
    do {
      path = build_shortest_path_to_dot();
      // cout << path << endl;
      walk(path);
      output += path;
    } while (path != "");
  }

  string find_best_compression(int n, char dir) {
    int size = n;
    string header;
    for (int i = 4; i <= n / 2; ++i) {
      if (n % (2 * i) != 0) continue;

      string tmp;
      int steps = n / (2 * i);
      for (int j = 0; j <= steps; ++j) {
        tmp += "B$ L# ";
      }
      tmp += "S";
      tmp.append(i, converted_dirs[dir]);
      tmp += " v!";
      if (tmp.size() < size) {
        size = tmp.size();
        header = tmp;
      }
    }
    return header;
  }

  string convert(string path) {
    string res;
    for (char c: path) {
      res.push_back(converted_dirs[c]);
    }
    return res;
  }

  string compress(string path) {
    int l = 0;
    int r = 0;
    string tmp = "";
    string cur = "";
    bool first_time = true;
    for (r = 1; r < path.size(); ++r) {
      if (path[r] != path[l]) {
        if (r-l >= 32) {
          auto comp = find_best_compression(r - l, path[l]);
          if (!comp.empty()) {
            if (first_time) {
              first_time = false;
              if (cur.size()) {
                tmp += "B. S" + convert(cur);
              }
              tmp += " B$ L# B. v# v# " + comp.substr(6);
            } else {
              if (cur.size()) {
                tmp += "B. S" + convert(cur) + " ";
              }
              tmp += comp;
            }
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
    if (cur.size()) {
      tmp = "B. " + tmp;
      if (!first_time) {
        tmp += " S";
      }
      tmp += convert(cur);
    }
    return tmp;
  }

  string solve() {
    solve_by_closest_dot();
    string compressed = compress(output);
    if (compressed.size() < output.size()) {
      output = compressed;
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
};

void solve_all() {
  for (int i = 11; i <= 11; ++i) {
    string input = "../inputs/lambdaman/" + to_string(i);
    string output = "../outputs/lambdaman/" + to_string(i);
    string score_path = "../scores/lambdaman/" + to_string(i);
    ifstream score_ifs(score_path);
    int bestScore = MAX_STEPS+1;
    score_ifs >> bestScore;
    score_ifs.close();

    ifstream ifs(input);
    LambdaMan hero(ifs);
    ifs.close();


    // string ans = hero.solve();
    string test;
    test.push_back('D');
    test.append(32, 'U');
    test.push_back('D');
    cout << test << endl;
    cout << hero.compress(test) << endl;

    // int score = ans.size();

    // if (score < bestScore) {
    //   cout << "Task " << i << " has new best score: " << score << ", previous best was: " << bestScore << endl;
    //   bestScore = score;
    //   ofstream ofs(output);
    //   ofs << ans;
    //   ofs.close();

    //   ofstream score_ofs(score_path);
    //   score_ofs << bestScore;
    //   score_ofs.close();
    // } else {
    //   cout << "Task " << i << " " << score << "/" << bestScore << endl;
    // }
  }
}


int main(int argc, char **argv) {
  // ios::sync_with_stdio(false);
  // cin.tie(0);

  solve_all();
  return 0;
}