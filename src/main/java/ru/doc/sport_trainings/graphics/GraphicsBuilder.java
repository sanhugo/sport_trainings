package ru.doc.sport_trainings.graphics;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.stereotype.Component;
import ru.doc.sport_trainings.DTO.ExerciseNoteGraphDTO;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GraphicsBuilder {
    public GraphicsBuilder()    {}
    public JFreeChart createChart(List<ExerciseNoteGraphDTO> exerciseNoteGraphDTOList, String exerciseType)
    {
        TimeSeriesCollection dataset = createDataset(exerciseNoteGraphDTOList);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("График упражнения "+ exerciseType,"Даты", "Масса", dataset,false,false,false);
        var plot = chart.getXYPlot();
        var yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setNumberFormatOverride(new java.text.DecimalFormat("#0.00"));
        var xAxis = (DateAxis) plot.getDomainAxis();
        xAxis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yyyy"));// два знака после запятой

        return chart;
    }
    public static TimeSeriesCollection createDataset(List<ExerciseNoteGraphDTO> exerciseNoteGraphDTOList)
    {
        TimeSeries dataset = new TimeSeries("Values");
        for (ExerciseNoteGraphDTO exerciseNoteGraphDTO : exerciseNoteGraphDTOList) {
            LocalDate d = exerciseNoteGraphDTO.getDate();
            dataset.add(new Day(d.getDayOfMonth(),d.getMonthValue(),d.getYear()),exerciseNoteGraphDTO.getTimes());
        }
        TimeSeriesCollection series = new TimeSeriesCollection();
        series.addSeries(dataset);
        return series;
    }
}
