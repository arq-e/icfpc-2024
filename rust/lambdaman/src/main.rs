#![cfg_attr(not(debug_assertions), windows_subsystem = "windows")] // hide console window on Windows in release

use eframe::egui;

pub mod lambdaman;
use lambdaman::Lambdaman;

#[cfg(not(target_arch = "wasm32"))]
fn main() -> Result<(), eframe::Error> {
    env_logger::init(); // Log to stderr (if you run with `RUST_LOG=debug`).
    let options = eframe::NativeOptions {
        viewport: egui::ViewportBuilder::default().with_inner_size([1920.0, 1080.0]),
        ..Default::default()
    };
    eframe::run_native(
        "Visualizer",
        options,
        Box::new(|cc| {
            // This gives us image support:
            egui_extras::install_image_loaders(&cc.egui_ctx);
            Box::new(Visualizer::new(cc))
        }),
    )
}

#[cfg(target_arch = "wasm32")]
fn main() {
    // Redirect `log` message to `console.log` and friends:
    eframe::WebLogger::init(log::LevelFilter::Debug).ok();

    let web_options = eframe::WebOptions::default();

    wasm_bindgen_futures::spawn_local(async {
        let start_result = eframe::WebRunner::new()
            .start(
                "Visualizer",
                web_options,
                Box::new(|cc| Box::new(Visualizer::new(cc))),
            )
            .await;
        let loading_text = eframe::web_sys::window()
            .and_then(|w| w.document())
            .and_then(|d| d.get_element_by_id("loading_text"));
        match start_result {
            Ok(_) => {
                loading_text.map(|e| e.remove());
            }
            Err(e) => {
                loading_text.map(|e| {
                    e.set_inner_html(
                        "<p> The app has crashed. See the developer console for details. </p>",
                    )
                });
                panic!("failed to start eframe: {e:?}");
            }
        }
    });
}

#[derive(serde::Deserialize, serde::Serialize)]
#[serde(default)]
struct Visualizer {
    data: Lambdaman,
    map_id: usize,
    time_to_next_tick: std::time::Duration,
    #[serde(skip)]
    last_tick: std::time::Instant
}

impl Visualizer {
    pub fn new(cc: &eframe::CreationContext<'_>) -> Self {
        if let Some(storage) = cc.storage {
            return eframe::get_value(storage, eframe::APP_KEY).unwrap_or_default();
        }

        Default::default()
    }
}

impl Default for Visualizer {
    fn default() -> Self {
        Self { data: Lambdaman::new(1),
            map_id: 1,
            time_to_next_tick: std::time::Duration::from_millis(100),
            last_tick: std::time::Instant::now() }
    }
}

impl eframe::App for Visualizer {
    fn save(&mut self, storage: &mut dyn eframe::Storage) {
        eframe::set_value(storage, eframe::APP_KEY, self);
    }

    fn update(&mut self, ctx: &egui::Context, _frame: &mut eframe::Frame) {
        let now = std::time::Instant::now();

        if now - self.last_tick > self.time_to_next_tick {
            self.last_tick = now;
            self.data.tick();
            ctx.request_repaint();
        }

        egui::CentralPanel::default().show(ctx, |ui| {
            egui::Grid::new("map picker").show(ui, |ui| {
                for i in 1..=Lambdaman::MAX_ID {
                    if Lambdaman::get_input_path(i).exists() && Lambdaman::get_output_path(i).exists() {
                        ui.selectable_value(&mut self.map_id, i, i.to_string())
                        .clicked().then(|| {
                            self.map_id = i;
                            self.data = Lambdaman::new(i);
                         });
                    }
                }
            });

            let width = 10.0;
            egui::ScrollArea::both().show_rows(ui, width, self.data.grid.len(), |ui, row_range| {
                egui::Grid::new("grid")
                    .min_col_width(width)
                    .max_col_width(width)
                    .spacing([0.0, 0.0])
                    .show(ui, |ui| {
                    let cols = self.data.grid[0].len();
                    let size = self.data.img_size;
                    for row in row_range {
                        for col in 0..cols {
                            let color_image = match self.data.grid[row][col] {
                                b'L' => self.data.hero.clone(),
                                b'#' => self.data.wall.clone(),
                                b'.' => self.data.item.clone(),
                                _ => self.data.blank.clone()
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