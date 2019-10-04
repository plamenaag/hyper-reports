package hyper.reports.model;

import hyper.reports.Constants;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileInfo {

    private Path filePath;
    private Instant date;
    private String dateString;
    private String company;

    public FileInfo(Path filePath) {
        this.filePath = filePath;
        this.date = null;
        this.dateString = null;
        this.company = null;
    }

    public FileInfo(String filePath) {
        this(Paths.get(filePath));
    }

    public String getFileName() {
        if (filePath != null) {
            return filePath.getFileName().toString();
        } else {
            return null;
        }
    }

    public File getFile() {
        if (getAbsolutePath() != null) {
            return new File(getAbsolutePath());
        } else {
            return null;
        }
    }

    private String getDateString() {
        if (dateString == null) {
            if (getFileName() != null && !getFileName().isEmpty()) {
                String datePattern = Constants.DATE_PATTERN;
                Pattern pattern = Pattern.compile(datePattern);
                Matcher matches = pattern.matcher(getFileName());
                if (matches.find() && matches.group(0) != null && !matches.group(0).isEmpty()) {
                    dateString = matches.group(0);
                }
            }
        }

        return dateString;
    }

    public Instant getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null && getDateString() != null) {
            try {
                date = simpleDateFormat.parse(getDateString()).toInstant();
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        }

        return date;
    }

    public String getCompanyName() {
        if (company == null && getFileName() != null && getDateString() != null
                && getDate() != null && getFileExtension() != null) {
            company = getFileName().replace(getDateString() + "-", "");
            company = company.replace("." + getFileExtension(), "");
        }

        return company;
    }

    public String getFileExtension() {
        if (getFileName() == null || getFileName().isEmpty()) {
            return null;
        } else {
            String[] parts = getFileName().split("\\.");
            if (parts.length > 0) {
                return parts[parts.length - 1];
            } else {
                return null;
            }
        }
    }

    public String getAbsolutePath() {
        if (filePath != null) {
            return filePath.toAbsolutePath().toString();
        } else {
            return null;
        }
    }
}

