/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filestudio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.examples.Archiver;
import org.apache.commons.compress.archivers.examples.Expander;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author Admin
 */
public class ArchiveExtractor {

    String path, outputPath;

    public ArchiveExtractor() {
        //empty constructor
    }

    public ArchiveExtractor(String path, String outputPath) {
        this.path = path;
        this.outputPath = outputPath;
    }

    public void setSelectedDir(String newDest) {
        this.path = newDest;//the selected folder to compress (can not be changed when compressing)
    }

    public void setOutputPath(String outDir) {
        this.outputPath = outDir;//the output zip file (can not be changed when compressing)
    }

    //to use this class (zip compression), call this method after creating instance and passing path. not createZip
    public void processFiles() throws IOException {
        File outputZip = new File(outputPath);
        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(outputZip);
        createZip(zipOut, path);
        zipOut.close();
    }

    public void createZip(ZipArchiveOutputStream outStream, String srcPath) throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(srcPath))) {
            List<File> fileList = (List<File>) walk.filter(Files::isRegularFile).map(x -> x.toFile()).collect(Collectors.toList());
            for (File file : fileList) {
                int index = file.getAbsoluteFile().getParent().lastIndexOf(File.separator);
                String parentDir = file.getAbsoluteFile().getParent().substring(index + 1);
                ZipArchiveEntry zaEntry = new ZipArchiveEntry(parentDir + File.separator + file.getName());
                zaEntry.setMethod(ZipEntry.DEFLATED);
                outStream.putArchiveEntry(zaEntry);
                IOUtils.copy(new FileInputStream(file), outStream);
                outStream.closeArchiveEntry();
            }
        }
    }

    public void createTarFile(String theFolder, String outputFile) throws IOException {
        // Create TAR file stream. outputFile should be a new .tar file (path), theFolder is the dir we compress
        try (TarArchiveOutputStream archive = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {

            File folderToTar = new File(theFolder);

            // Walk through files, folders & sub-folders.
            Files.walk(folderToTar.toPath()).forEach((Path p) -> {
                File file = new File(p.toString());

                // Directory is not streamed, but its files are streamed into TAR file with
                // folder in it's path
                if (!file.isDirectory()) {
                    System.out.println("Taring file - " + file);
                    TarArchiveEntry entry_1 = new TarArchiveEntry(file, file.getName());
                    try (FileInputStream fis = new FileInputStream(file)) {
                        archive.putArchiveEntry(entry_1);
                        IOUtils.copy(fis, archive);
                        archive.closeArchiveEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Complete archive entry addition.
            archive.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTarGZipFile(String theTar, String outputFile) {
        //creates a tar.gz given an existing tar, where outputFile should be a path ending with.tar.gz
        try (GzipCompressorOutputStream gzipOutput = new GzipCompressorOutputStream(
                new FileOutputStream(outputFile))) {
            File tarFile = new File(theTar);
            gzipOutput.write(Files.readAllBytes(tarFile.toPath()));
            gzipOutput.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTarBZip2File(String theTar, String outputFile) {
        //creates a bzip2 tar.gz given an existing tar, where outputFile should be a path ending with.tar.gz
        try (BZip2CompressorOutputStream gzipOutput = new BZip2CompressorOutputStream(
                new FileOutputStream(outputFile))) {

            File tarFile = new File(theTar);
            gzipOutput.write(Files.readAllBytes(tarFile.toPath()));

            gzipOutput.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTarXZFile(String theTar, String outputFile) {
        //creates a tar.xz given an existing tar, where outputFile should be a path ending with.tar.xz
        try (XZCompressorOutputStream gzipOutput = new XZCompressorOutputStream(
                new FileOutputStream(outputFile))) {

            File tarFile = new File(theTar);
            gzipOutput.write(Files.readAllBytes(tarFile.toPath()));

            gzipOutput.finish();
        } catch (IOException e) {
            System.out.println("ABU-XZ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createTarSnappyFile(String theTar, String outputFile) {
        //creates a tar.sz given an existing tar, where outputFile should be a path ending with.tar.sz
        try (FramedSnappyCompressorOutputStream gzipOutput = new FramedSnappyCompressorOutputStream(
                new FileOutputStream(outputFile))) {

            File tarFile = new File(theTar);
            gzipOutput.write(Files.readAllBytes(tarFile.toPath()));

            gzipOutput.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTarDeflateFile(String theTar, String outputFile) {
        //creates a tar.deflate given an existing tar, where outputFile should be a path ending with.tar.deflate
        try (DeflateCompressorOutputStream gzipOutput = new DeflateCompressorOutputStream(
                new FileOutputStream(outputFile))) {

            File tarFile = new File(theTar);
            gzipOutput.write(Files.readAllBytes(tarFile.toPath()));

            gzipOutput.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //unfinnished & NOT USABLE [ABU]
//    public static void unGZipUnTarFile() {
//        /*
//	 * Un GZip file to extract TAR file.
//         */
//        try (GzipCompressorInputStream archive = new GzipCompressorInputStream(
//                new BufferedInputStream(new FileInputStream("output/sample.tar.gz")))) {
//
//            OutputStream out = Files.newOutputStream(Paths.get("output/un-gzipped.tar"));
//            IOUtils.copy(archive, out);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        /*
//	 * Untar extracted TAR file
//         */
//        try (TarArchiveInputStream archive = new TarArchiveInputStream(
//                new BufferedInputStream(new FileInputStream("output/un-gzipped.tar")))) {
//
//            TarArchiveEntry entry;
//            while ((entry = archive.getNextTarEntry()) != null) {
//
//                File file = new File("output/" + entry.getName());
//                System.out.println("Untaring - " + file);
//                // Create directory before streaming files.
//                String dir = file.toPath().toString().substring(0, file.toPath().toString().lastIndexOf("\\"));
//                Files.createDirectories(new File(dir).toPath());
//                // Stream file content
//                IOUtils.copy(archive, new FileOutputStream(file));
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
    //cannot extract multifomat archiver such as ".tar.xxx"
    public void unarchive(String theFile, String outputDir) {
        Path arch = Paths.get(theFile);
        Path outDir = Paths.get(outputDir);
        try {
            new Expander().expand(arch, outDir);
        } catch (IOException ex) {
            System.out.println("ABU-UNARCH(ioe): " + ex.getMessage());
            ex.printStackTrace();
            //Logger.getLogger(ArchiveExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArchiveException ex) {
            System.out.println("ABU-UNARCH2(arch-e): " + ex.getMessage());
            ex.printStackTrace();
            //Logger.getLogger(ArchiveExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void archive(String theFolder, String destDir) {
        Path pDir = Paths.get(theFolder);
        Path pDestDir = Paths.get(destDir);
        String format = FileNameUtils.getExtension(pDestDir);
        try {
            new Archiver().create(format, pDestDir, pDir);
        } catch (IOException ex) {
            //System.out.println("ABU-ARCH(ioe): " + ex.getMessage());
            //Logger.getLogger(ArchiveExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArchiveException ex) {
            //System.out.println("ABU-ARCH(arch-e): " + ex.getMessage());
            //Logger.getLogger(ArchiveExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
