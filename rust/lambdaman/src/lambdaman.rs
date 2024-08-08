const INPUT_PATH: &'static str = "../../inputs/lambdaman/";
const OUTPUT_PATH: &'static str = "../../outputs/lambdaman/";

use std::fs::File;
use std::path::PathBuf;
use std::io::{self, BufRead};

use eframe::egui::ColorImage;
use eframe::egui::Color32;

#[derive(serde::Deserialize, serde::Serialize)]
#[serde(default)]
#[derive(Default)]
pub struct Lambdaman {
    pub grid: Vec<Vec<u8>>,
    pub path: String,
    pub x: usize,
    pub y: usize,
    pub step: usize,
    pub hero: ColorImage,
    pub item: ColorImage,
    pub wall: ColorImage,
    pub blank: ColorImage,
    pub img_size: usize,
}

impl Lambdaman {
    pub const MAX_ID: usize = 21;

    pub fn new(id: usize) -> Self {
        assert!(1 <= id && id <= Self::MAX_ID);
        let mut m = Self { grid: Self::load_grid(Self::get_input_path(id)),
                            path: Self::load_path(Self::get_output_path(id)),
                            x: 0, y: 0, step: 0,
                            hero: Self::create_image(Color32::from_rgb(112, 163, 204)), // light blue
                            item: Self::create_image(Color32::from_rgb(0, 255, 0)),
                            wall: Self::create_image(Color32::from_rgb(255, 0, 0)),
                            blank: Self::create_image(Color32::from_rgb(255, 255, 0)),
                            img_size: 20,};

        for row in 0..m.grid.len() {
            for col in 0..m.grid[row].len() {
                if m.grid[row][col] == b'L' {
                    m.x = col;
                    m.y = row;
                }
            }
        }
        m
    }

    pub fn tick(&mut self) {
        if self.step < self.path.len() {
            self.grid[self.y][self.x] = b' ';

            let c = self.path.chars().nth(self.step).unwrap();
            match c {
                'U' => self.y -= 1,
                'D' => self.y += 1,
                'L' => self.x -= 1,
                'R' => self.x += 1,
                _ => panic!("Unknown direction")
            }
            self.step += 1;
            self.grid[self.y][self.x] = b'L';
        }
    }

    pub fn get_input_path(id: usize) -> PathBuf {
        let mut path = PathBuf::new();
        path.push(INPUT_PATH);
        path.push(id.to_string());
        path
    }

    pub fn get_output_path(id: usize) -> PathBuf {
        let mut path = PathBuf::new();
        path.push(OUTPUT_PATH);
        path.push(id.to_string());
        path
    }

    // pub fn get_texture(&self, row: usize, col: usize) -> SizedTexture {
    //     let color = match self.grid[row][col] {
    //         b'L' => Color32::from_rgb(112, 163, 204), // light blue
    //         b'#' => Color32::from_rgb(255, 0, 0),
    //         b'.' => Color32::from_rgb(0, 255, 0),
    //         _ => Color32::from_rgb(255, 255, 0)
    //     };
    //     let 
    //     SizedTexture::new(20.0, 20, Self::create_image(color))
    // }

    fn create_image(color: Color32) -> ColorImage {
        ColorImage::new([1, 1], color)
    }

    fn load_grid(path: PathBuf) -> Vec<Vec<u8>> {
        let mut v = Vec::new();
        for line in io::BufReader::new(File::open(&path).unwrap()).lines().flatten() {
            if !line.is_empty() {
                v.push(line.bytes().collect());
            }
        }
        v
    }

    fn load_path(path: PathBuf) -> String {
        io::BufReader::new(File::open(&path).unwrap()).lines().flatten().collect()
    }
}