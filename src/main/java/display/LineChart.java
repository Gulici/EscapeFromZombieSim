package display;

import org.jfree.data.category.CategoryDataset;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;

import static sim.Stats.dead;
import static sim.Stats.escaped;

public class LineChart extends JPanel {
    public LineChart(String chartTitle ,String state,int x,int y) {
        JFreeChart lineChart = ChartFactory.createLineChart(chartTitle,"","",
                createDataset(state),PlotOrientation.VERTICAL, false,false,false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setBounds(x,y,200,150);


    }

    private CategoryDataset createDataset(String state) {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        switch (state) {
            case "escaped":
                for (Long i : escaped.keySet()) {
                    data.addValue(escaped.get(i), "Escaped", i);
                }
                break;
            case "dead":
                for(Long i : dead.keySet()) {
                    data.addValue(dead.get(i),"Dead", i);
                }
                break;
        }
        return data;
    }




}