#include <bits/stdc++.h>
#include <argparse/argparse.hpp>
#define ll long long

using namespace std;

const int MAX_STEPS = 10000000;

struct SpaceShip {
  int x = 0;
  int y = 0;
  int vx = 0;
  int vy = 0;
  int steps = MAX_STEPS;
  vector<pair<int, int>> targets;
  vector<vector<int>> increment_to_choose;
  ifstream &in;
  string output;
  SpaceShip(ifstream &ifs) : in(ifs) {
    int a, b;
    while (in >> a >> b) {
      targets.push_back({a, b});
    }
    // for (auto target : targets) {
    //   cout << target.first << " " << target.second << endl;
    // }

    increment_to_choose = vector<vector<int>>(3, vector<int> (3, 0));
    increment_to_choose[0][0] = 1;
    increment_to_choose[0][1] = 4;
    increment_to_choose[0][2] = 7;
    increment_to_choose[1][0] = 2;
    increment_to_choose[1][1] = 5;
    increment_to_choose[1][2] = 8;
    increment_to_choose[2][0] = 3;
    increment_to_choose[2][1] = 6;
    increment_to_choose[2][2] = 9;
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

      move(increment_to_choose[change_vx+1][change_vy+1]);
    };
  }

  void acc_move(const pair<int, int> &target) {
    while (steps > 0) {
      int diff_x = target.first - x;
      int diff_y = target.second - y;
      int dist = abs(diff_x) + abs(diff_y);

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

      move(increment_to_choose[change_vx+1][change_vy+1]);
    };
  }

  void solve_basic() {
    for (auto target : targets) {
      basic_move(target);
    }
  }

  void solve_basic_with_nearest() {
    while (auto id = pick_nearest_id() != -1) {
      auto target = targets[id];
      basic_move(targets[id]);
      targets.erase(targets.begin() + id);
    }
  }

  int pick_nearest_id() {
    int best_dist = INT_MAX;
    int best_id = -1;
    for (int i = 0; i < targets.size(); ++i) {
      auto target = targets[i];
      int diff_x = target.first - x;
      int diff_y = target.second - y;
      int dist = abs(diff_x) + abs(diff_y);
      if (dist < best_dist) {
        best_dist = dist;
        best_id = i;
      }
    }
    return best_id;
  }

  string solve() {
    solve_basic_with_nearest();
    return output;
  }

  void move(int increment) {
    // cout << x << " " << y << " " << vx << " " << vy << " " << increment << endl;
    switch (increment) {
      case 1: --vx; --vy; break;
      case 2: --vy; break;
      case 3: ++vx; --vy; break;
      case 4: --vx; break;
      case 5: break;
      case 6: ++vx; break;
      case 7: --vx; ++vy; break;
      case 8: ++vy; break;
      case 9: ++vx; ++vy; break;
    }

    x += vx;
    y += vy;
    output.push_back('0' + increment);
    --steps;
  }

};

void solve_all() {
  for (int i = 1; i <= 10; ++i) {
    string input = "../inputs/spaceship/" + to_string(i);
    string output = "../outputs/spaceship/" + to_string(i);
    string score_path = "../scores/spaceship/" + to_string(i);
    ifstream score_ifs(score_path);
    int bestScore = MAX_STEPS+1;
    score_ifs >> bestScore;
    score_ifs.close();

    ifstream ifs(input);
    SpaceShip s(ifs);
    ifs.close();


    string ans = s.solve();

    int score = ans.size();

    if (score < bestScore) {
      cout << "Task " << i << " has new best score: " << score << ", previous best was: " << bestScore << endl;
      bestScore = score;
      ofstream ofs(output);
      ofs << ans;
      ofs.close();

      ofstream score_ofs(score_path);
      score_ofs << bestScore;
      score_ofs.close();
    } else {
      cout << "Task " << i << " " << score << "/" << bestScore << endl;
    }
  }
}


int main(int argc, char **argv) {
  // ios::sync_with_stdio(false);
  // cin.tie(0);

  // argparse::ArgumentParser program("spaceship");

  // string input;
  // string output;

  // program.add_argument("-i", "--input").required().help("input json").store_into(input);
  // program.add_argument("-o", "--output").required().help("output json").store_into(output);

  // try {
  //   program.parse_args(argc, argv);
  // }
  // catch (const std::exception& err) {
  //   std::cerr << err.what() << std::endl;
  //   std::cerr << program;
  //   return 1;
  // }

  // cout << "input: " << input << endl;
  // cout << "output: " << output << endl;

  // ifstream ifs(input);
  // ofstream ofs(output);
  // SpaceShip s(ifs, ofs);
  // s.solve();

  // ifs.close();
  // ofs.close();

  solve_all();
  return 0;
}