package app.classes;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphCreator {
    public static void createGraph(List<double[]> data) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        for (int i = 0; i < data.size(); i++) {
            XYSeries series = new XYSeries("Série " + (i + 1));
            double[] row = data.get(i);
            for (int j = 0; j < row.length; j++) {
                series.add(j, row[j]);
            }
            dataset.addSeries(series);
        }
        
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Gráfico de Dados do CSV",
                "Amostra",
                "Valores",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        JFrame frame = new JFrame("Gráfico");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
