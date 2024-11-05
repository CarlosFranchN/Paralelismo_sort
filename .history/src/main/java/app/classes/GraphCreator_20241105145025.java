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
    public static void createGraph(List<double[]> data, String title) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        for (int i = 0; i < data.size(); i++) {
            XYSeries series = new XYSeries("Amostra " + (data.get(i)[0]));
            double[] row = data.get(i);
            for (int j = 1; j < row.length; j++) {
                System.out.println("x " + j);
                System.out.println("y " + row[j]);
                series.add(j, row[j]);
            }
            dataset.addSeries(series);
        }
        
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Grafico de Dados do CSV " + title,
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
            String nThreads ;
            double tempo = dataset.getYValue(series, item);
            int value = (int) dataset.getXValue(series, item); // Converte para int
            switch (value) {
                case 1:
                    nThreads = "Seq";
                    break;
                case 2:
                    nThreads = "2 t";
                    break;
                case 3:
                    nThreads = "4 t";
                    break;
                case 4:
                    nThreads = "5 t";
                    break;
                case 5:
                    nThreads = "10 t";
                    break;
                case 6:
                    nThreads = "100 t";
                    break;
                case 7:
                    nThreads = "1000 t";
                    break;
                case 8:
                    nThreads = "10000 t";
                    break;
                case 9:
                    nThreads = "";
                    break;
                default:
                    throw new AssertionError();
            }
            // System.out.println(dataset.getXValue(series, item));
            // System.out.println(tempo);
            try {
                  // Pega o valor Y
                return String.format(nThreads +" : %.0f microsegs", tempo);  // Simplesmente retorna o valor de Y
            } catch (Exception e) {
                System.out.println("Erro ao gerar o label: " + e.getMessage());
                return "";
            }
                
                // return String.format("%s %.0f microsegs", nThreads,tempo);
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
