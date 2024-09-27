# Compressor_Decompressor

## Description

The "Java Swing-based Compressor and Decompressor" project is a software application developed in Java that provides a user-friendly graphical user interface (GUI) for compressing and decompressing files using the GZip algorithm. The primary goal of this project is to offer an intuitive and efficient tool for users to reduce the size of files or directories and subsequently restore them to their original state without any lossy conversions.

## Tech Stack

- **Java:** The core programming language used to develop the application.
- **Java Swing:** The GUI framework employed to design and implement the user interface.
- **GZip Algorithm:** A popular compression algorithm used to reduce the size of files.

## Key Features

### Compress Files

- The application allows users to select individual files or entire directories for compression using the GZip algorithm.
- Users can specify the destination location for the compressed file(s).
- The compression process is carried out in the background, with a progress bar or status indicator to show the progress to the user.
- Once the compression is completed, users receive a notification, and the compressed file is saved at the specified location.
- The compression process ensures that there are no lossy conversions, preserving the integrity of the original file content.

### Decompress Files

- Users can select GZip-compressed files and decompress them to their original format.
- The decompression process is initiated by selecting the compressed file and specifying the destination folder for the decompressed files.
- Progress of the decompression process is displayed, and users are notified when the operation is completed.
- The decompression process ensures that there are no lossy conversions, ensuring the original data is accurately reconstructed.

### User-friendly GUI

- The GUI is designed using Java Swing, providing a visually appealing and intuitive interface for users to interact with the application.
- The application offers buttons, menus, and file selection dialogs to enable users to navigate and interact with files easily.

### Error Handling

- The application handles various error scenarios, such as file not found, invalid file formats, and disk space limitations.
- Informative error messages are displayed to users to assist in troubleshooting and resolving issues.

### Multi-threading

- To ensure a smooth user experience, the application employs multi-threading to perform file compression and decompression tasks in the background, preventing the GUI from becoming unresponsive during lengthy operations.

### File Integrity Checks

- The application validates the integrity of compressed files during decompression to ensure that the original files are accurately reconstructed.

## Download Links

- [Download JAR](https://github.com/Anurag-996/Compressor_Decompressor/raw/main/CompressorDecompressor.jar)
- [Download EXE](https://github.com/Anurag-996/Compressor_Decompressor/raw/main/Compressor_Decompressor.exe)

---

Overall, the Java Swing-based Compressor and Decompressor using the GZip algorithm provides a user-friendly and efficient solution for compressing and decompressing files in a familiar and intuitive manner. Users can easily manage file size and share data more efficiently, making it a valuable tool for various applications. The project ensures that no lossy conversions occur during compression and decompression, preserving the content integrity of the files.
