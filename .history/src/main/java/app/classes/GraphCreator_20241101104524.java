package app.classes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
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
        chartPanel.setPreferredSize(new Dimension(1400,800));
                // Configuração do renderer para adicionar rótulos
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setDefaultItemLabelsVisible(true); // Ativa a visibilidade dos rótulos
        renderer.setDefaultItemLabelGenerator(new XYItemLabelGenerator() {
        @Override
        public String generateLabel(XYDataset dataset, int series, int item) {
            String nThreads = "";
            double tempo = dataset.getYValue(series, item);
            switch (dataset.getYValue(series, item)) {
                case (double)0 -> nThreads = "Seq";
                case (double)1 -> nThreads = "2 t";
                case (double)2 -> nThreads = "4 t";
                case (double)3 -> nThreads = "5 t";
                case (double)4 -> nThreads = "10 t";
                case (double)5 -> nThreads = "100 t";
                case (double)6 -> nThreads = "1000 t";
                case (double)7 -> nThreads = "10000 t";
                default -> throw new AssertionError();
            }
            // System.out.println(nThreads);
            // System.out.println(tempo);

                
                return String.format(nThreads+"%.0f microsegs", dataset.getYValue(series, item));
            }
        });
        renderer.setDefaultToolTipGenerator(new StandardXYToolTipGenerator(
            "{0}: X={1}, Y={2}",  // Formato do texto do tooltip
            java.text.NumberFormat.getIntegerInstance(),
            java.text.NumberFormat.getNumberInstance()
        ));
        chart.getXYPlot().setRenderer(renderer);

        JFrame frame = new JFrame("Grafico de Tempo por nThreads");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        

    }
}
