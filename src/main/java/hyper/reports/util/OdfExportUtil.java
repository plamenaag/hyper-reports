package hyper.reports.util;

import org.jopendocument.dom.spreadsheet.SpreadSheet;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.util.List;
public class OdfExportUtil {

    public static void exportToOds(List<Object[]> list, String[] columns, String fileName) {

        Object[][] data = new Object[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }

        TableModel model = new DefaultTableModel(data, columns);

        final File file = new File(fileName);
        try {
            SpreadSheet.createEmpty(model).saveAs(file);
        } catch (IOException e) {
            System.err.println("Export failed: " + fileName);
        }
    }
}
