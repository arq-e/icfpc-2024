#include <bits/stdc++.h>
#include <argparse/argparse.hpp>
#define ll long long

using namespace std;

const int MAX_STEPS = 10000000;

template <typename T> int sgn(T val) {
    return (T(0) < val) - (val < T(0));
}

struct SpaceShip {
  int x = 0;
  int y = 0;
  int vx = 0;
  int vy = 0;
  int steps = MAX_STEPS;
  set<pair<int, int>> targets;
  ifstream &in;
  string output;
  bool success = true;
  SpaceShip(ifstream &ifs) : in(ifs) {
    int a, b;
    while (in >> a >> b) {
      targets.insert({a, b});
    }
  }

  void basic_move(const pair<int, int> &target) {
    bool firstTime = true;
    while (steps > 0) {
      int diff_x = target.first - x;
      int diff_y = target.second - y;
      int dist = abs(diff_x) + abs(diff_y);
      if (firstTime) {
        // cout << "x " << x << ",  y " << y << ",  vx " << vx << ",  vy " << vy << ", target.x " << target.first << ",  target.y " << target.second << endl;
        // cout << diff_x << " " << diff_y << endl;
        firstTime = false;
      }

      if (dist == 0) {
        break;
      }

      int target_vx = (diff_x < 0) ? -1 :
                      (diff_x > 0) ? 1 : 0;
      int target_vy = (diff_y < 0) ? -1 :
                      (diff_y > 0) ? 1 : 0;

      int change_vx = (vx < target_vx) ? 1 : 
                      (vx > target_vx) ? -1 : 0;
      int change_vy = (vy < target_vy) ? 1 :
                      (vy > target_vy) ? -1 : 0;

      move(change_vx, change_vy);
    };
  }

  int get_change(const vector<int> &pattern, int step) {
    if (step == 0) {
      return 1;
    }
    if (step == pattern.size()) {
      return -1;
    }
    if (step > pattern.size()) {
      return 0;
    }
    return pattern[step] - pattern[step - 1];
  }

  void increment_move(const pair<int, int> &target) {
    int diff_x = target.first - x;
    int diff_y = target.second - y;

    vector<int> inc_x = calc_increment_pattern(abs(diff_x));
    vector<int> inc_y = calc_increment_pattern(abs(diff_y));

    int max_steps = max(inc_x.size(), inc_y.size());
    if (max_steps > steps) {
      return;
    }
    for (int step = 0; step <= max_steps; ++step) {
      int change_vx = get_change(inc_x, step) * sgn(diff_x);
      int change_vy = get_change(inc_y, step) * sgn(diff_y);
      move(change_vx, change_vy);
    }
    if (target.first - x != 0 || target.second - y != 0) {
      cout << "Missed target by: " << target.first - x << " " << target.second - y << endl;
      exit(1);
    }
  }

  bool can_jump(const pair<int, int> &target) {
    int diff_x = target.first - x;
    int diff_y = target.second - y;
    int change_vx = diff_x - vx;
    int change_vy = diff_y - vy;
    if (abs(change_vx) <= 1 && abs(change_vy) <= 1) {
      return true;
    }
    return false;
  }

  bool jump(const pair<int, int> &target) { // solver failed if false
    int diff_x = target.first - x;
    int diff_y = target.second - y;
    int change_vx = diff_x - vx;
    int change_vy = diff_y - vy;
    if (abs(change_vx) <= 1 && abs(change_vy) <= 1) {
      move(change_vx, change_vy);
      // cout << "Jumped to " << target.first << " " << target.second << endl;
    } else {
      cout << "Can't jump to " << target.first << " " << target.second << endl;
      return false;
    }
    return true;
  }

  static vector<int> calc_increment_pattern(size_t dist) {
    if (dist == 0) {
      return {};
    }
    if (dist == 1) {
      return {1};
    }
    int max_digit = floor(sqrt(dist));
    vector<int> pattern;
    pattern.reserve(max_digit * 2 + 1); // two may be unused
    int sum = 0;
    for (int i = 1; i <= max_digit; ++i) {
      pattern.push_back(i);
      sum += i;
    }
    for (int i = max_digit - 1; i >= 1; --i) {
      pattern.push_back(i);
      sum += i;
    }
    int remain = dist - sum;

    if (remain > 0) {
      pattern.push_back(0);
      for (int i = max_digit * 2 - 1; i >= max_digit; --i) {
        ++pattern[i];
        if (--remain == 0) {
          break;
        }
      }
    }
    if (remain > 0) {
      pattern.push_back(0);
      for (int i = max_digit * 2; i > max_digit; --i) {
        ++pattern[i];
        if (--remain == 0) {
          break;
        }
      }
    }
    return pattern;
  }

  vector<int> shortest_pattern_for_one(int dx, int speed) {
    if (dx == 0 && speed == 0) {
      return {};
    }
    vector<int> pattern;
    if (sgn(dx) == sgn(speed)) {
      
    }
    if (speed == 0) {
      
    }
  }

  static vector<vector<int>> calc_shortest_movement_pattern(const pair<int, int> &target, const pair<int, int> &pos, const pair<int, int> &velocity) {
    int diff_x = target.first - pos.first;
    int diff_y = target.second - pos.second;
    int dist = abs(diff_x) + abs(diff_y);
    if (dist == 0) {
      return {};
    }
  }

  void solve_basic() {
    for (auto target : targets) {
      basic_move(target);
    }
  }

  void solve_basic_with_nearest() {
    while (targets.size() > 0) {
      auto t = pick_nearest();
      if (!t.has_value()) {
        success = false;
        return;
      }
      basic_move(t.value());
      targets.erase(t.value());
    }
  }

  void solve_with_increment_for_nearest() {
    srand(time(0));
    while (targets.size() > 0) {
      auto t = pick_nearest();
      // auto t = pick_nearest_with_random_bonus(50);
      if (!t.has_value()) {
        success = false;
        return;
      }
      increment_move(t.value());
      targets.erase(t.value());
    }
  }

  void solve_by_closest_jump() {  // best score for 1, 12
    while (targets.size() > 0) {
      auto t = pick_jumpable();
      if (!t.has_value()) {
        success = false;
        return;
      }
      if (!jump(t.value())) {
        success = false;
        return;
      }
      targets.erase(t.value());
    }
  }

  optional<pair<int, int>> pick_nearest() {
    return pick_best([this](int tx, int ty) { return -max(abs(tx-x), abs(ty-y)); });
  }
  optional<pair<int, int>> pick_nearest_with_random_bonus(int max_bonus = 0) {
    return pick_best([this, max_bonus](int tx, int ty) { return -max(abs(tx-x), abs(ty-y)) + (rand() % max_bonus); });
  }
  optional<pair<int, int>> pick_jumpable() {
    return pick_best([this](int tx, int ty) { return can_jump({tx, ty}) ? -max(abs(tx-x), abs(ty-y)) : INT_MIN; });
  }


  optional<pair<int, int>> pick_best(function <int(int, int)> func) {
    int best_score = INT_MIN;
    optional<pair<int, int>> best = nullopt;
    for (auto &t : targets) {
      int score = func(t.first, t.second);
      if (score > best_score) {
        best_score = score;
        best = t;
      }
    }
    return best;
  }

  optional<string> solve() {
    // solve_by_closest_jump();
    solve_with_increment_for_nearest();
    // solve_with_increment_for_farthest();
    // solve_basic();
    return success ? optional<string>(output) : nullopt;
  }

  void move(int change_vx, int change_vy) {
    vx += change_vx;
    vy += change_vy;
    x += vx;
    y += vy;
    output.push_back('1' + (change_vx+1) + (change_vy+1)*3);
    --steps;
  }
};

void solve_all(int start, int end) {
  for (int i = start; i <= end; ++i) {
    string input = "../inputs/spaceship/" + to_string(i);
    string output = "../outputs/spaceship/" + to_string(i);
    string score_path = "../scores/spaceship/" + to_string(i);
    ifstream score_ifs(score_path);
    int bestScore = MAX_STEPS+1;
    score_ifs >> bestScore;
    score_ifs.close();

    ifstream ifs(input);
    if (!ifs) {
      cout << "File not found: " << input << endl;
      continue;
    }
    SpaceShip s(ifs);
    ifs.close();


    auto ans = s.solve();
    if (!ans.has_value()) {
      cout << "Solver for task " << i << " failed" << endl;
      continue;
    }


    int score = ans.value().size();

    if (score < bestScore) {
      cout << "Task " << i << " has new best score: " << score << ", previous best was: " << bestScore << endl;
      bestScore = score;
      ofstream ofs(output);
      ofs << ans.value();
      ofs.close();

      ofstream score_ofs(score_path);
      score_ofs << bestScore;
      score_ofs.close();
    } else {
      cout << "Task " << i << " " << score << "/" << bestScore << endl;
    }
  }
}

void calc_increment_pattern_test() {
  for (int i = 1; i < 100; ++i) {
    auto v = SpaceShip::calc_increment_pattern(i);
    for (int i = 0; i < v.size(); ++i) {
      cout << v[i] << " ";
    }
    cout << endl;
  }
}


int main(int argc, char **argv) {
  solve_all(1, 24);
  // calc_increment_pattern_test();
  return 0;
}