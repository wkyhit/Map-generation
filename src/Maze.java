import java.io.File;
import java.io.IOException;

import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Maze extends JFrame implements ActionListener {

    private String path = System.getProperty("user.dir");

    private InitDfsPanel dfsPanel;
    private InitPrimPanel primPanel;
    private InitEllerPanel ellerPanel;

    /*界面面板*/
    private JPanel panel;

    private JPanel centerPanel; //主区域面板
    private JPanel dfsCenterPanel;//dfs迷宫面板
    private JPanel primCenterPanel;//prim迷宫面板
    private JPanel ellerCenterPanel;//eller迷宫面板
    /*界面面板*/

    /*迷宫数组*/
    private MazeGrid dfsGrid[][];
    private MazeGrid primGrid[][];
    private MazeGrid ellerGrid[][];
    /*迷宫数组*/

    /*迷宫指示按钮*/

    /*界面按钮*/
    private JButton dfsReStart;
    private JButton dfsStart;
    private JButton primReStart;
    private JButton primStart;
    private JButton ellerReStart;
    private JButton ellerStart;
    /*界面按钮*/
    private JTextField sizeInput;//大小输入框

    private int rows = 17;// rows 和cols目前暂定只能是奇数
    private int cols = 17;
    private List<String> willVisit; //保存待访问的当前块的邻接块
    private LinkedList<String> comed;
    private long startTime;
    private long endTime;

    private Maze() {
        willVisit = new ArrayList<>();
        comed = new LinkedList<>();
        init();
        this.setTitle("迷宫生成程序");
        ImageIcon icon = new ImageIcon("icon.png"); //图片和项目同一路径，故不用图片的路径
        this.setIconImage(icon.getImage());
        this.add(panel);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void savePic(String path, String picType) {
        try {
            switch (picType) {
                case "dfs":
                    getScreen(path, this.dfsCenterPanel);
                    break;
                case "prim":
                    getScreen(path, this.primCenterPanel);
                    break;
                case "eller":
                    getScreen(path, this.ellerCenterPanel);
                    break;
            }
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getScreen(String path, JPanel dfsCenterPanel) throws AWTException, IOException {
        BufferedImage dfsImage;
        dfsImage = new Robot().createScreenCapture(
                new Rectangle(dfsCenterPanel.getX() + 8, dfsCenterPanel.getY() + 65, dfsCenterPanel.getWidth(), dfsCenterPanel.getHeight() + 5));
        ImageIO.write(dfsImage, "png", new File(path));
    }

    private void setSize(int size) {
        this.rows = size;
        this.cols = size;
    }

    private void init() {
        panel = new JPanel();

        //control按钮面板
        JPanel controlPanel = new JPanel();
        //底部功能面板
        JPanel funcPanel = new JPanel();
        //控制迷宫大小面板
        JPanel sizePanel = new JPanel();
        //迷宫类型提示面板
        JPanel typePanel = new JPanel();
        centerPanel = new JPanel();
        dfsCenterPanel = new JPanel();
        primCenterPanel = new JPanel();
        ellerCenterPanel = new JPanel();
        panel.setLayout(new BorderLayout()); //方位布局
        dfsReStart = new JButton("regenerate the dfsMaze");//dfs迷宫 restart按钮
        dfsStart = new JButton("dfsStart to explore");
        primReStart = new JButton("regenerate the primMaze");
        primStart = new JButton("primStart to explore");
        ellerReStart = new JButton("regenerate the ellerMaze");
        ellerStart = new JButton("ellerStart to explore");

        //大小确认按钮
        JButton sizeButton = new JButton("commit");
        sizeInput = new JTextField();//大小输入框

        //迷宫名称显示按钮
        /*迷宫指示按钮*/
        JButton DFS = new JButton("dfsMaze");
        JButton PRIM = new JButton("primMaze");
        JButton ELLER = new JButton("ellerMaze");

        //保存图片按钮
        JButton saveImage = new JButton("save image");

        primGrid = new MazeGrid[rows][cols];
        dfsGrid = new MazeGrid[rows][cols];
        ellerGrid = new MazeGrid[rows][cols];


        InitDfsPanel dfsPanel = new InitDfsPanel(rows, cols);
        InitPrimPanel primPanel = new InitPrimPanel(rows, cols);
        InitEllerPanel ellerPanel = new InitEllerPanel(rows, cols);
        this.dfsPanel = dfsPanel;
        this.primPanel = primPanel;
        this.ellerPanel = ellerPanel;

        dfsGrid = dfsPanel.getMaze();
        primGrid = primPanel.getMaze();
        ellerGrid = ellerPanel.getMaze();

        dfsCenterPanel = dfsPanel.getMazePanel();
        primCenterPanel = primPanel.getMazePanel();
        ellerCenterPanel = ellerPanel.getMazePanel();

        //中心迷宫面板添加
        centerPanel.setLayout(new GridLayout(1, 3, 40, 0));//三个迷宫间距水平：40 纵向: 0
        centerPanel.add(dfsCenterPanel);
        centerPanel.add(primCenterPanel);
        centerPanel.add(ellerCenterPanel);

        controlPanel.setLayout(new GridLayout(1, 6, 40, 0));
        controlPanel.add(dfsReStart); //添加dfs restart button
        controlPanel.add(dfsStart); //添加dfs start button
        controlPanel.add(primReStart);//添加prim restart button
        controlPanel.add(primStart);//添加prim start button
        controlPanel.add(ellerReStart);
        controlPanel.add(ellerStart);

        /*功能面板和大小控制面板*/
        funcPanel.setLayout(new GridLayout(2, 1));
        funcPanel.add(typePanel);
        funcPanel.add(sizePanel);
        typePanel.setLayout(new GridLayout(1, 3, 40, 0));
        typePanel.add(DFS);
        typePanel.add(PRIM);
        typePanel.add(ELLER);
        sizePanel.setLayout(new FlowLayout());
        sizePanel.add(sizeInput);
        sizePanel.add(sizeButton);
        sizePanel.add(saveImage);

        sizeInput.setColumns(10);

        panel.add(controlPanel, BorderLayout.NORTH); //上方加入控制面板
        panel.add(centerPanel, BorderLayout.CENTER); //中心放置迷宫
        panel.add(funcPanel, BorderLayout.SOUTH);

        /*增加按钮事件监听*/
        dfsStart.addActionListener(this);
        dfsReStart.addActionListener(this);
        primStart.addActionListener(this);
        primReStart.addActionListener(this);
        ellerStart.addActionListener(this);
        ellerReStart.addActionListener(this);
        saveImage.addActionListener(this);
        sizeButton.addActionListener(this);
    }


    /*走迷宫*/
    private String goMaze(MazeGrid mazeGrid[][], int x, int y) {
        int comeX = 0;
        int comeY = 0;
        // left
        if (x - 1 >= 0) {
            if (mazeGrid[x - 1][y].isMark()) {
                if (!comed.contains((x - 1) + "#" + y))
                    willVisit.add((x - 1) + "#" + y);
            }
        }
        // right
        if (x + 1 < cols) {
            if (mazeGrid[x + 1][y].isMark()) {
                if (!comed.contains((x + 1) + "#" + y))
                    willVisit.add((x + 1) + "#" + y);
            }
        }
        // up
        if (y - 1 >= 0) {
            if (mazeGrid[x][y - 1].isMark()) {
                if (!comed.contains(x + "#" + (y - 1)))
                    willVisit.add(x + "#" + (y - 1));
            }
        }
        // down
        if (y + 1 < rows) {
            if (mazeGrid[x][y + 1].isMark()) {
                if (!comed.contains(x + "#" + (y + 1)))
                    willVisit.add(x + "#" + (y + 1));
            }
        }
        if (!willVisit.isEmpty()) {
            int visit = (int) (Math.random() * willVisit.size());
            String id = willVisit.get(visit);
            comeX = Integer.parseInt(id.split("#")[0]);
            comeY = Integer.parseInt(id.split("#")[1]);
            come(mazeGrid, x, y, comeX, comeY);
            willVisit.clear();
            comed.add(x + "#" + y);
        } else {
            if (!comed.isEmpty()) {
                String id = comed.removeLast();
                comeX = Integer.parseInt(id.split("#")[0]);
                comeY = Integer.parseInt(id.split("#")[1]);
                come(mazeGrid, x, y, comeX, comeY);
                comed.addFirst(x + "#" + y);
            }
        }
        return comeX + "#" + comeY;
    }

    private void come(MazeGrid[][] mazeGrid, int x, int y, int comeX, int comeY) {
        mazeGrid[x][y].setPersonCome(false);
        mazeGrid[comeX][comeY].setPersonCome(true);
        mazeGrid[x][y].repaint();
        mazeGrid[comeX][comeY].repaint();
    }

    private static boolean isNumeric(String str) {
        if (str != null && !"".equals(str.trim()))
            return str.matches("^[0-9]*$");
        else
            return false;
    }

    private int comeX = 0;
    private int comeY = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "save image": {
                savePic(this.path + "/dfspic.png", "dfs");
                savePic(this.path + "/primpic.png", "prim");
                savePic(this.path + "/ellerpic.png", "eller");
                JOptionPane.showMessageDialog(null, "save image success!", "提示", JOptionPane.PLAIN_MESSAGE);
                System.out.println("保存成功!");
                break;
            }
            case "commit": {
                String text = sizeInput.getText();
                if (isNumeric(text)) {
                    int size = Integer.parseInt(text);
                    if (size % 2 != 0 && size > 0 && size < 40) {

                        setSize(size);

                        dfsReStart.setVisible(false);
                        dfsStart.setVisible(false);
                        primReStart.setVisible(false);
                        primStart.setVisible(false);
                        ellerReStart.setVisible(false);
                        ellerStart.setVisible(false);

                        long start = System.currentTimeMillis();
                        refreshMap("dfs");
                        refreshMap("prim");
                        refreshMap("eller");
                        long end = System.currentTimeMillis();
                        dfsReStart.setVisible(true);
                        dfsStart.setVisible(true);
                        primReStart.setVisible(true);
                        primStart.setVisible(true);
                        ellerReStart.setVisible(true);
                        ellerStart.setVisible(true);
                        System.out.println("生成3个迷宫总耗时：" + (end - start) + "毫秒");
                    } else {
                        JOptionPane.showMessageDialog(null, "请输入小于40的奇正整数！", "提示", JOptionPane.PLAIN_MESSAGE);
                        sizeInput.setText("");
                    }
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "请输入奇正整数！", "提示", JOptionPane.PLAIN_MESSAGE);
                    break;
                }
            }

            case "regenerate the dfsMaze": {
                dfsReStart.setVisible(false);
                dfsStart.setVisible(false);
                primReStart.setVisible(false);
                primStart.setVisible(false);
                ellerReStart.setVisible(false);
                ellerStart.setVisible(false);

                long start = System.currentTimeMillis();
                refreshMap("dfs");
                long end = System.currentTimeMillis();
                dfsReStart.setVisible(true);
                dfsStart.setVisible(true);
                primReStart.setVisible(true);
                primStart.setVisible(true);
                ellerReStart.setVisible(true);
                ellerStart.setVisible(true);
                System.out.println("生成DFS迷宫耗时：" + (end - start) + "毫秒");
                break;
            }
            case "dfsStart to explore": {
                startTime = System.currentTimeMillis();
                dfsStart.setVisible(false);
                dfsReStart.setText("prohibit to refresh");
                primReStart.setVisible(false);
                primStart.setVisible(false);
                ellerReStart.setVisible(false);
                ellerStart.setVisible(false);
                int delay = 100; //former:1000

                int period = 50;// former:500 循环间隔

                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (dfsGrid[rows - 1][cols - 1].isPersonCome()) {
                            endTime = System.currentTimeMillis();
                            JOptionPane.showMessageDialog(null, "走出DFS迷宫耗时："
                                            + (endTime - startTime) / 1000 + "s(" + (endTime - startTime) + "ms)", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                            centerPanel.setVisible(false);
                            dfsGrid[0][0].setVisited();
                            dfsGrid[rows - 1][cols - 1].setPersonCome(false);
                            dfsGrid[0][0].setPersonCome(true);
                            dfsGrid[0][0].setStart();

                            comeX = 0;
                            comeY = 0;
                            willVisit.clear();
//                            visited.clear();
                            comed.clear();

                            dfsReStart.setText("regenerate the dfsMaze");
                            dfsReStart.setVisible(true);
                            dfsStart.setVisible(true);
                            primReStart.setVisible(true);
                            primStart.setVisible(true);
                            ellerStart.setVisible(true);
                            ellerReStart.setVisible(true);

                            centerPanel.setVisible(true);
                            this.cancel();
                        } else {
                            String id = goMaze(dfsGrid, comeX, comeY);
                            comeX = Integer.parseInt(id.split("#")[0]);
                            comeY = Integer.parseInt(id.split("#")[1]);
                        }
                    }
                }, delay, period);
                break;
            }
            case "regenerate the primMaze": {

                dfsReStart.setVisible(false);
                dfsStart.setVisible(false);
                primReStart.setVisible(false);
                primStart.setVisible(false);
                ellerReStart.setVisible(false);
                ellerStart.setVisible(false);
                long start = System.currentTimeMillis();
                refreshMap("prim");
                long end = System.currentTimeMillis();
                dfsReStart.setVisible(true);
                dfsStart.setVisible(true);
                primReStart.setVisible(true);
                primStart.setVisible(true);
                ellerReStart.setVisible(true);
                ellerStart.setVisible(true);
                System.out.println("生成Prim迷宫耗时：" + (end - start) + "毫秒");
                break;
            }
            case "primStart to explore": {
                startTime = System.currentTimeMillis();
                dfsReStart.setVisible(false);
                dfsStart.setVisible(false);
                primReStart.setText("prohibit to refresh");
                primStart.setVisible(false);
                ellerReStart.setVisible(false);
                ellerStart.setVisible(false);

                int delay = 100; //former:1000

                int period = 50;// former:500 循环间隔

                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (primGrid[rows - 1][cols - 1].isPersonCome()) {
                            endTime = System.currentTimeMillis();
                            JOptionPane.showMessageDialog(null, "走出Prim迷宫耗时："
                                            + (endTime - startTime) / 1000 + "s(" + (endTime - startTime) + "ms)", "提示",
                                    JOptionPane.ERROR_MESSAGE);

                            primGrid[0][0].setVisited();
                            primGrid[rows - 1][cols - 1].setPersonCome(false);
                            primGrid[0][0].setPersonCome(true);
                            primGrid[0][0].setStart();

                            comeX = 0;
                            comeY = 0;
                            willVisit.clear();
//                            visited.clear();
                            comed.clear();

                            primReStart.setText("regenerate the primMaze");
                            primReStart.setVisible(true);
                            primStart.setVisible(true);
                            dfsReStart.setVisible(true);
                            dfsStart.setVisible(true);
                            ellerReStart.setVisible(true);
                            ellerStart.setVisible(true);
                            this.cancel();
                        } else {
                            String id = goMaze(primGrid, comeX, comeY);
                            comeX = Integer.parseInt(id.split("#")[0]);
                            comeY = Integer.parseInt(id.split("#")[1]);
                        }
                    }
                }, delay, period);
                break;
            }
            case "regenerate the ellerMaze": {

                dfsReStart.setVisible(false);
                dfsStart.setVisible(false);
                primReStart.setVisible(false);
                primStart.setVisible(false);
                ellerReStart.setVisible(false);
                ellerStart.setVisible(false);

                long start = System.currentTimeMillis();
                refreshMap("eller");
                long end = System.currentTimeMillis();
                dfsReStart.setVisible(true);
                dfsStart.setVisible(true);
                primReStart.setVisible(true);
                primStart.setVisible(true);
                ellerReStart.setVisible(true);
                ellerStart.setVisible(true);
                System.out.println("生成eller迷宫耗时：" + (end - start) + "毫秒");
                break;
            }
            case "ellerStart to explore": {
                startTime = System.currentTimeMillis();
                dfsReStart.setVisible(false);
                dfsStart.setVisible(false);
                primReStart.setVisible(false);
                primStart.setVisible(false);
                ellerReStart.setText("prohibit to refresh");
                ellerStart.setVisible(false);
                int delay = 100; //former:1000

                int period = 50;// former:500 循环间隔

                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (ellerGrid[rows - 1][cols - 1].isPersonCome()) {
                            endTime = System.currentTimeMillis();
                            JOptionPane.showMessageDialog(null, "走出Eller迷宫耗时："
                                            + (endTime - startTime) / 1000 + "s(" + (endTime - startTime) + "ms)", "提示",
                                    JOptionPane.ERROR_MESSAGE);

                            ellerGrid[0][0].setVisited();
                            ellerGrid[rows - 1][cols - 1].setPersonCome(false);
                            ellerGrid[0][0].setPersonCome(true);
                            ellerGrid[0][0].setStart();

                            comeX = 0;
                            comeY = 0;
                            willVisit.clear();
//                            visited.clear();
                            comed.clear();

                            dfsReStart.setVisible(true);
                            dfsStart.setVisible(true);
                            primReStart.setVisible(true);
                            primStart.setVisible(true);
                            ellerReStart.setText("regenerate the ellerMaze");
                            ellerReStart.setVisible(true);
                            ellerStart.setVisible(true);
                            this.cancel();
                        } else {
                            String id = goMaze(ellerGrid, comeX, comeY);
                            comeX = Integer.parseInt(id.split("#")[0]);
                            comeY = Integer.parseInt(id.split("#")[1]);
                        }
                    }
                }, delay, period);
                break;
            }
        }
    }

    private void refreshMap(String mazeType) {

        comeX = 0;
        comeY = 0;
        willVisit.clear();
//        visited.clear();
        comed.clear();
//        wallList.clear();

        switch (mazeType) {
            case "dfs":
                centerPanel.setVisible(false);
                centerPanel.removeAll();
                centerPanel.setLayout(new GridLayout(1, 3, 40, 1));
                InitDfsPanel dfsPanel = new InitDfsPanel(rows, cols);
                dfsCenterPanel = dfsPanel.getMazePanel();
                this.dfsPanel = dfsPanel;
                primCenterPanel = this.primPanel.getMazePanel();
                ellerCenterPanel = this.ellerPanel.getMazePanel();
                dfsGrid = dfsPanel.getMaze();
                centerPanel.add(dfsCenterPanel);
                centerPanel.add(primCenterPanel);
                centerPanel.add(ellerCenterPanel);
                centerPanel.setVisible(true);
                break;
            case "prim":
                centerPanel.setVisible(false);
                centerPanel.removeAll();
                centerPanel.setLayout(new GridLayout(1, 3, 40, 1));
                InitPrimPanel primPanel = new InitPrimPanel(rows, cols);
                dfsCenterPanel = this.dfsPanel.getMazePanel();
                primCenterPanel = primPanel.getMazePanel();
                this.primPanel = primPanel;
                ellerCenterPanel = this.ellerPanel.getMazePanel();
                primGrid = primPanel.getMaze();
                centerPanel.add(dfsCenterPanel);
                centerPanel.add(primCenterPanel);
                centerPanel.add(ellerCenterPanel);
                centerPanel.setVisible(true);
                break;
            case "eller":
                centerPanel.setVisible(false);
                centerPanel.removeAll();
                centerPanel.setLayout(new GridLayout(1, 3, 40, 1));
                InitEllerPanel ellerPanel = new InitEllerPanel(rows, cols);
                ellerCenterPanel = ellerPanel.getMazePanel();
                dfsCenterPanel = this.dfsPanel.getMazePanel();
                primCenterPanel = this.primPanel.getMazePanel();
                this.ellerPanel = ellerPanel;
                ellerGrid = ellerPanel.getMaze();
                centerPanel.add(dfsCenterPanel);
                centerPanel.add(primCenterPanel);
                centerPanel.add(ellerCenterPanel);
                centerPanel.setVisible(true);
                break;

        }

    }

    public static void main(String args[]) {
        long start = System.currentTimeMillis();
        new Maze();
        long end = System.currentTimeMillis();
        System.out.println("生成迷宫耗时：" + (end - start) + "毫秒");
    }
}
