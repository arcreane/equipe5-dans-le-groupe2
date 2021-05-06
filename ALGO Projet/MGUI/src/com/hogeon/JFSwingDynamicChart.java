package com.hogeon;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.ValueAxis;  
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;  
import org.jfree.data.time.TimeSeriesCollection;  
import org.jfree.data.xy.XYDataset;

import com.google.gson.Gson;
import com.hogeon.utils.*;

public class JFSwingDynamicChart extends JPanel implements ActionListener {
	private TimeSeries series;
	private TimeSeries upLine;
	private TimeSeries downLine;
	private double lastValue = 100.0;
	public double MaxNum;
	public double MinNum;
	boolean isFirst=true;
	boolean isChange=false;
	/**
	 * 构造
	 */
	public JFSwingDynamicChart(double a,double b) {
		this.MaxNum=a;
		this.MinNum=b;
		//setBackground(Color.green);
	}

	/**
	 * 创建应用程序界面
	 */
	public void ValueChanged(double a,double b) {
		this.MaxNum=a;
		this.MinNum=b;
		this.upLine.clear();
		this.downLine.clear();
		isFirst=true;
		System.out.println("update range success!");
	}
	
	public void createUI() {
		//this.series.setDescription("Math.random()-随机数据");
		this.series = new TimeSeries("Humidity", Millisecond.class);
		TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
		this.upLine = new TimeSeries("upLine", Millisecond.class);
        dataset.addSeries(this.upLine);
        this.downLine = new TimeSeries("downLine", Millisecond.class);
        dataset.addSeries(this.downLine);
		ChartPanel chartPanel = new ChartPanel(createChart(dataset));
		chartPanel.setPreferredSize(new java.awt.Dimension(900, 600));
//		JPanel buttonPanel = new JPanel();
//		buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		add(chartPanel);
//		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * 根据结果集构造JFreechart报表对象
	 * 
	 * @param dataset
	 * @return
	 */
	private JFreeChart createChart(XYDataset dataset) {
		JFreeChart result = ChartFactory.createTimeSeriesChart("Swing-Dynamic Chart", "Now Time",
				"Value/(%RH)", dataset, true, true, false);
		XYPlot plot = (XYPlot) result.getPlot();
		
		XYLineAndShapeRenderer xylinerenderer=(XYLineAndShapeRenderer)plot.getRenderer();
		xylinerenderer.setSeriesPaint(0, new Color(255,0  ,0 ));
	    xylinerenderer.setSeriesPaint(1, new Color(0 ,0, 255 ));
	    xylinerenderer.setSeriesPaint(2, new Color(125, 0, 100));
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(100000.0);
		axis = plot.getRangeAxis();
		axis.setRange(0.00,100.00);
		return result;
	}

	public void actionPerformed(ActionEvent e) {
	}

	/**
	 * 动态运行
	 */
	@SuppressWarnings("unchecked")
	public void dynamicRun() {
		while (true) {
			//获取服务器最新data数据
			String url = "http://127.0.0.1:6080/DataRequest";
			try {
				String temp=HttpUtil.get(url);
				try {
					Thread.currentThread().sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Map<String, String> params = new HashMap<>();
				if (StringUtils.isNotEmpty(temp)) {
					try {
						params = new Gson().fromJson(temp, HashMap.class);
					} catch (Exception e) {
					}
				}
				if(temp!=null&&params.containsKey("message")){
					String ans=params.get("message");
					System.out.println(temp+" "+ans);
					String[] list=ans.split("&");
					this.lastValue=Double.valueOf(list[0]);
					double a=Double.valueOf(list[1]);
					double b=Double.valueOf(list[2]);
					if(Math.abs(a-this.MaxNum)>1e-6||Math.abs(b-this.MinNum)>1e-6){
						System.out.println("different!");
						this.ValueChanged(a, b);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			this.series.add(new Millisecond(), this.lastValue*100);
			this.upLine.add(new Millisecond(),this.MaxNum*100);
			this.downLine.add(new Millisecond(),this.MinNum*100);
			if(isFirst){
				Millisecond f=new Millisecond(0,0,0,0,1,1,2001);
				this.upLine.add(f,this.MaxNum*100);
				this.downLine.add(f,this.MinNum*100);
				isFirst=false;
			}
			
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

