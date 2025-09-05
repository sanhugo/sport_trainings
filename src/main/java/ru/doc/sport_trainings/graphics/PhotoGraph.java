package ru.doc.sport_trainings.graphics;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PhotoGraph {
    public static byte[] toPNG(JFreeChart chart, int width, int height) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, width, height);
        return out.toByteArray();
    }
}
