package org.jumpmind.boot;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.jumpmind.boot.BootConstants.RESPONSE_CODE_UPDATE_AVAILABLE;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateJob implements Runnable {

    Logger logger = Logger.getLogger(UpdateJob.class.getName());
    BootConfig bootConfig;
    AppConfig appConfig;

    public UpdateJob(BootConfig bootConfig, AppConfig appConfig) {
        this.bootConfig = bootConfig;
        this.appConfig = appConfig;
    }

    @Override
    public void run() {
        update();
    }

    public boolean update() {
        try {
            if (hasUpdate()) {
                URL url = new URL(String.format("%s/update/deviceid/%s/version/%s", bootConfig.getUpdateUrl(), bootConfig.getDeviceId(),
                        appConfig.getVersion()));
                DeployConfig deployConfig = new DeployConfig(url, this.bootConfig);

                Path targetPath = Paths.get(String.format("%s/%s", bootConfig.getAppDir(), deployConfig.getVersion()));
                if (!"?".equals(appConfig.getVersion()) && !deployConfig.isOverwrite()) {
                    Path sourcePath = Paths.get(String.format("%s/%s", bootConfig.getAppDir(), appConfig.getVersion()));
                    Files.walk(sourcePath).forEach(source -> copy(source, targetPath.resolve(sourcePath.relativize(source))));
                } else {
                    if (targetPath.toFile().exists()) {
                        // start with a fresh directory
                        Files.walk(targetPath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    }
                    Files.createDirectories(targetPath);
                }

                List<File> installedFiles = new ArrayList<>();
                gatherInstalledFiles(installedFiles, targetPath.toFile());

                List<Artifact> artifacts = deployConfig.getArtifacts();
                for (Artifact artifact : artifacts) {
                    File newFile = new File(targetPath.toFile(), artifact.getName());
                    if (!newFile.exists() || artifact.isOverwrite()) {
                        newFile.getParentFile().mkdirs();
                        URL fileUrl = new URL(String.format("%s/download/deviceid/%s/version/%s/%s", bootConfig.getUpdateUrl(),
                                bootConfig.getDeviceId(), deployConfig.getVersion(), artifact.getName()));
                        logger.info(String.format("Downloading %s from %s", newFile.getAbsolutePath(), fileUrl));
                        transferFile(fileUrl, newFile);
                    } else {
                        logger.info(String.format("File already exists.  No need to download %s", newFile.getAbsolutePath()));
                    }
                    installedFiles.remove(newFile);
                }

                for (File installedFile : installedFiles) {
                    logger.info(String.format("File was not present in the latest version.  Removing %s", installedFile.getAbsolutePath()));
                    installedFile.delete();

                }

                deployConfig.store(new File(targetPath.toFile(), "deploy.json"));
                appConfig.setVersion(deployConfig.getVersion());
                appConfig.store();

                return true;
            } else {
                logger.info("Up to date");
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to complete update process", e);
            return false;
        }
    }

    void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    void gatherInstalledFiles(List<File> list, File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                list.add(file);
            } else if (file.isDirectory()) {
                gatherInstalledFiles(list, file);
            }
        }
    }

    void transferFile(URL url, File targetFile) throws Exception {
        ReadableByteChannel readableByteChannel = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        try {
            readableByteChannel = Channels.newChannel(url.openStream());
            fileOutputStream = new FileOutputStream(targetFile);
            fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } finally {
            close(fileChannel);
            close(fileOutputStream);
            close(readableByteChannel);
        }
    }

    boolean hasUpdate() {
        try {
            String checkUrl = String.format("%s/check/deviceid/%s/version/%s", bootConfig.getUpdateUrl(), bootConfig.getDeviceId(),
                    appConfig.getVersion());
            HttpURLConnection connection = (HttpURLConnection) new URL(checkUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            return code == RESPONSE_CODE_UPDATE_AVAILABLE;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
