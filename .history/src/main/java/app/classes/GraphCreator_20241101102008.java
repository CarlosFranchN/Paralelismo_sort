package app.classes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphCreator {
    public static void createGraph(List<double[]> data) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        for (int i = 0; i < data.size(); i++) {
            XYSeries series = new XYSeries("Amostra " + (data.get(i)[0]));
            double[] row = data.get(i);
            for (int j = 0; j < row.length; j++) {
                series.add(j, row[j]);
            }
            dataset.addSeries(series);
        }
        
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Gr?fico de Dados do CSV",
                "NThreads",
                "Tempos",
                dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1200,600));
                // Configuração do renderer para adicionar rótulos
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setDefaultItemLabelsVisible(true); // Ativa a visibilidade dos rótulos
        renderer.setDefaultItemLabelGenerator(new XYItemLabelGenerator() {
            @Override
            public String generateLabel(XYDataset dataset, int series, int item) {
                
                return String.format("%.0f microsegs", dataset.getYValue(series, item));
            }
        });
        // renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.PLAIN, 12)); // Define a fonte dos rótulos

        // Define o renderer no gráfico
        chart.getXYPlot().setRenderer(renderer);

        JFrame frame = new JFrame("Gr?fico");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
