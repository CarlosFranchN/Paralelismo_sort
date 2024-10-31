package app.classes;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graph2 extends JPanel {
    XYSeriesCollection dataset;

    public Graph2(int dimensions, double[] xValues, double[] yValues, String[] xLabels, String[] yLabels, String amostra) {
        dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Tempos com Array["+ amostra +"]");
        


        // Adiciona os valores ao conjunto de dados
        for (int i = 0; i < xValues.length; i++) {
            series.add(xValues[i], yValues[i]);
        }

        dataset.addSeries(series);

        // Cria o gráfico
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(dimensions, dimensions));
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }

    public JFreeChart createChart(XYSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Título do Gráfico", // Título
                "Eixo X", // Label do eixo X
                "Eixo Y", // Label do eixo Y
                dataset // Dados
        );
    
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
    
        // Criação do item label generator
        XYItemLabelGenerator generator = (XYDataset dataset1, int series, int item) -> {
            // Obtendo o valor do eixo X
            double xValue = dataset1.getXValue(series, item);
            // Retornando o valor do eixo X como string, com a unidade "micro" adicionada
            return String.format("%.2f micro", xValue); // Formata o valor com duas casas decimais
        };
        renderer.setDefaultItemLabelGenerator(generator);
        renderer.setDefaultItemLabelsVisible(true); // Habilita rótulos
        plot.setRenderer(renderer);
    
        return chart;
    }

    // Método para testar a classe
    public static void testGraph2() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Teste do Graph2");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Dados de teste
            double[] xValues = {10, 15, 24, 400};
            double[] yValues = {2.0, 3.5, 1.5, 4.0};
            String[] xLabels = {"Label 1", "Label 2", "Label 3", "Label 4"};
            String[] yLabels = {"Y1", "Y2", "Y3", "Y4"};

            Graph2 graph = new Graph2(1200, xValues, yValues, xLabels, yLabels,"Teste");
            frame.add(graph);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
