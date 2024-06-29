#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")] // hide console window on Windows in release

use eframe::egui;
use eframe::egui::ColorImage;
use eframe::egui::Color32;

use std::fs::File;
use std::path::PathBuf;
use std::io::{self, BufRead};

const INPUT_PATH: &'static str = "../../inputs/lambdaman/";
const OUTPUT_PATH: &'static str = "../../outputs/lambdaman/";
const MAX_ID: usize = 21;

struct Map {
    grid: Vec<Vec<u8>>,
    path: String,
    x: usize,
    y: usize,
    step: usize,
    hero: ColorImage,
    item: ColorImage,
    wall: ColorImage,
    blank: ColorImage,
    img_size: usize,
}

impl Map {
    pub fn new(id: usize) -> Self {
        assert!(1 <= id && id <= MAX_ID);
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

    fn create_image(color: Color32) -> ColorImage {
        ColorImage::new([1, 1], color)
    }

    fn load_grid(path: PathBuf) -> Vec<Vec<u8>> {
        let mut v = Vec::new();
        for line in io::BufReader::new(File::open(&path).unwrap()).lines().flatten() {
            v.push(line.bytes().collect());
        }
        v
    }

    fn load_path(path: PathBuf) -> String {
        io::BufReader::new(File::open(&path).unwrap()).lines().flatten().collect()
    }
}

fn main() -> Result<(), eframe::Error> {
    env_logger::init(); // Log to stderr (if you run with `RUST_LOG=debug`).
    let options = eframe::NativeOptions {
        viewport: egui::ViewportBuilder::default().with_inner_size([1920.0, 1080.0]),
        ..Default::default()
    };
    eframe::run_native(
        "My egui App",
        options,
        Box::new(|cc| {
            // This gives us image support:
            egui_extras::install_image_loaders(&cc.egui_ctx);

            Box::<MyApp>::new(MyApp::new())
        }),
    )
}

struct MyApp {
    map: Map,
    map_id: usize,
    time_to_next_tick: std::time::Duration,
    last_tick: std::time::Instant
}

impl MyApp {
    pub fn new() -> Self {
        Self { map: Map::new(1),
               map_id: 1,
               time_to_next_tick: std::time::Duration::from_millis(100),
               last_tick: std::time::Instant::now() }
    }
}

impl eframe::App for MyApp {
    fn update(&mut self, ctx: &egui::Context, _frame: &mut eframe::Frame) {
        let now = std::time::Instant::now();

        if now - self.last_tick > self.time_to_next_tick {
            self.last_tick = now;
            self.map.tick();
            ctx.request_repaint();
        }

        egui::CentralPanel::default().show(ctx, |ui| {
            egui::Grid::new("map picker").show(ui, |ui| {
                for i in 1..=MAX_ID {
                    if Map::get_input_path(i).exists() {
                        ui.selectable_value(&mut self.map_id, i, i.to_string())
                        .clicked().then(|| {
                            self.map_id = i;
                            self.map = Map::new(i);
                         });
                    }
                }
            });

            let width = 10.0;
            egui::ScrollArea::both().show_rows(ui, width, self.map.grid.len(), |ui, row_range| {
                egui::Grid::new("grid")
                    .min_col_width(width)
                    .max_col_width(width)
                    .spacing([0.0, 0.0])
                    .show(ui, |ui| {
                    let cols = self.map.grid[0].len();
                    let size = self.map.img_size;
                    for row in row_range {
                        for col in 0..cols {
                            let color_image = match self.map.grid[row][col] {
                                b'L' => self.map.hero.clone(),
                                b'#' => self.map.wall.clone(),
                                b'.' => self.map.item.clone(),
                                _ => self.map.blank.clone()
                            };
                            let handle = ctx.load_texture("hero", color_image, Default::default());
                            let sized_image = egui::load::SizedTexture::new(handle.id(), egui::vec2(size as f32, size as f32));
                            ui.image(sized_image);
                        }
                        ui.end_row();
                    }
                });
            })

        });
    }
}