```markdown
# Parallel Image Processing in Java

A college project demonstrating parallel programming concepts in Java by implementing multi-threaded image processing operations. Supports grayscale conversion, Gaussian blur, edge detection, and more, with performance benchmarking for sequential vs. parallel execution.

![Example: Grayscale and Edge Detection](examples/input_output.png) *(Optional example image)*

## Features
- **Parallelized Image Filters**:
  - Grayscale Conversion (luminosity method)
  - Gaussian Blur (kernel-based convolution)
  - Sobel Edge Detection (horizontal/vertical kernels)
  - Brightness/Contrast Adjustment
  - Image Rotation (90°, 180°, 270°)
- **Benchmarking Mode**: Compare sequential vs. parallel execution times.
- **Thread-Safe Operations**: Non-overlapping image regions for parallel writes.
- **Extensible Architecture**: Easily add new filters.

## Requirements
- Java JDK 8 or higher
- Maven (for dependency management, optional)

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Mek1402211/-parallel-programming-project.git
   cd parallel-image-processing
   ```
2. Compile the project:
   ```bash
   javac -d bin src/main/java/*.java
   ```

## Usage
### Command-Line Interface
Process an image with a specified filter:
```bash
java -cp bin ImageProcessor --input input.jpg --output output.jpg --filter grayscale
```

#### Supported Filters:
- `grayscale`
- `blur` (e.g., `--radius 3` for kernel size)
- `edge` (Sobel operator)
- `brightness` (e.g., `--value 20` to increase brightness)
- `rotate` (e.g., `--angle 90`)

#### Benchmarking:
Add `--benchmark` to compare performance:
```bash
java -cp bin ImageProcessor --input input.jpg --output output.jpg --filter blur --radius 2 --benchmark
```
Example output:
```
[SEQUENTIAL] Time: 1200 ms
[PARALLEL] Time: 350 ms
Speedup: 3.43x
```

### Code Example (Custom Filter)
```java
public class CustomFilter {
    public static BufferedImage apply(BufferedImage image) {
        BufferedImage output = new BufferedImage(...);
        // Use ForkJoinPool or parallel streams
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ImageTask(image, output, 0, 0, image.getWidth(), image.getHeight()));
        return output;
    }
}
```

## Implementation Details
### Parallelization Strategies
- **Fork/Join Framework**: Split images into tiles (e.g., 100x100 pixels) using `RecursiveAction`.
  ```java
  class BlurTask extends RecursiveAction {
      // Splits image into quadrants recursively
  }
  ```
- **Parallel Streams**: Process rows concurrently with `IntStream.parallel()`.
- **Thread Pools**: Manually manage tasks with `ExecutorService`.

### Key Classes
- `ImageProcessor`: CLI entry point.
- `GrayscaleConverter`, `GaussianBlur`: Filter implementations.
- `BenchmarkUtils`: Measures execution times.

## Benchmark Results
| Filter          | Image Size    | Sequential Time | Parallel Time | Speedup |
|-----------------|---------------|-----------------|---------------|---------|
| Grayscale       | 1920x1080     | 220 ms          | 60 ms         | 3.67x   |
| Gaussian Blur   | 3840x2160     | 4500 ms         | 980 ms        | 4.59x   |
| Sobel Edge      | 1280x720      | 850 ms          | 230 ms        | 3.7x    |

## Future Enhancements
- Add GUI (JavaFX/Swing) for real-time previews.
- Support for additional filters (e.g., histogram equalization, emboss).
- Optimize thread pool sizing based on image dimensions.
- Integrate OpenCV for advanced operations.

## Contributing
1. Fork the repository.
2. Create a branch for your feature (`git checkout -b feature/amazing-filter`).
3. Commit changes (`git commit -m 'Add amazing filter'`).
4. Push to the branch (`git push origin feature/amazing-filter`).
5. Open a Pull Request.

## License
Distributed under the MIT License. See [LICENSE](LICENSE) for details.
```

---

### Customization Tips
1. **Add Screenshots**: Include an `examples/` folder with before/after images.
2. **Update Benchmarks**: Replace the table with your actual performance metrics.
3. **Expand Filters**: Document new filters if added (e.g., `--filter sharpen`).
4. **Credits**: Acknowledge contributors or references used.

This README provides a clear overview of your project’s goals, usage, and technical depth, making it easy for users and evaluators to understand. 😊
