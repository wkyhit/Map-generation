import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;

// 迷宫
@SuppressWarnings("serial")
public class Maze extends JFrame implements ActionListener {

    private String path = System.getProperty("user.dir");

    private InitDfsPanel dfsPanel;
    private InitPrimPanel primPanel;
    private InitEllerPanel ellerPanel;
//    private GoMaze goMaze;

    /*界面面板*/
    private JPanel panel;

    private JPanel sizePanel;//控制迷宫大小面板
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

    /*界面按钮*/
    private JButton dfsReStart;
    private JButton dfsStart;
    private JButton primReStart;
    private JButton primStart;
    private JButton ellerReStart;
    private JButton ellerStart;
    private JButton sizeButton;
    private JButton saveImage;//保存图片按钮
    /*界面按钮*/
    private JTextField sizeInput;//大小输入框

    private int rows = 25;// rows 和cols目前暂定只能是奇数
    private int cols = 25;
    private List<String> willVisit; //保存待访问的当前块的邻接块
    private List<String> visited;   //保存图中所有已访问过块
    private LinkedList<String> comed;
    private List<String> wallList; //prim算法墙链
    private long startTime;
    private long endTime;

    private Maze() {
        willVisit = new ArrayList<>();
        visited = new ArrayList<>();
        comed = new LinkedList<>();
        wallList = new LinkedList<>();
        init();
        this.setTitle("回溯法--走迷宫");
        ImageIcon icon = new ImageIcon("icon.png"); //图片和项目同一路径，故不用图片的路径
        this.setIconImage(icon.getImage());
        this.add(panel);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void savePic(String path, String picType) {
        BufferedImage dfsImage = null;
        BufferedImage primImage = null;
        BufferedImage ellerImage = null;
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
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getScreen(String path, JPanel dfsCenterPanel) throws AWTException, IOException {
        BufferedImage dfsImage;
        dfsImage = new Robot().createScreenCapture(
                new Rectangle(dfsCenterPanel.getX() + 8, dfsCenterPanel.getY() + 65, dfsCenterPanel.getWidth(), dfsCenterPanel.getHeight() + 5));
        ImageIO.write(dfsImage, "png", new File(path));
    }

    private void init() {
        panel = new JPanel();
        //control按钮面板
        JPanel controlPanel = new JPanel();
        sizePanel = new JPanel();
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

        sizeButton = new JButton("commit");
        sizeInput = new JTextField();//大小输入框

        saveImage = new JButton("save image");//保存迷宫图片

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

        /*大小控制面板*/
        sizeInput.setColumns(10);
        sizePanel.setLayout(new FlowLayout());
        sizePanel.add(sizeInput);
        sizePanel.add(sizeButton);
        sizePanel.add(saveImage);

        panel.add(controlPanel, BorderLayout.NORTH); //上方加入控制面板
        panel.add(centerPanel, BorderLayout.CENTER); //中心放置迷宫
        panel.add(sizePanel, BorderLayout.SOUTH);//下方放文本控制

        /*增加按钮事件监听*/
        dfsStart.addActionListener(this);
        dfsReStart.addActionListener(this);
        primStart.addActionListener(this);
        primReStart.addActionListener(this);
        ellerStart.addActionListener(this);
        ellerReStart.addActionListener(this);
        saveImage.addActionListener(this);
    }


    /**
     * 走迷宫
     */
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

    private int comeX = 0;
    private int comeY = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "save image": {
                savePic(this.path + "/dfspic.png", "dfs");
                savePic(this.path + "/primpic.png", "prim");
                savePic(this.path + "/ellerpic.png", "eller");
                System.out.println("save image success!");
                break;
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
                dfsReStart.setText("prohibit to refresh");
                dfsStart.setVisible(false);
                primReStart.setVisible(false);
                primStart.setVisible(false);
                ellerReStart.setVisible(false);
                ellerStart.setVisible(false);
                int delay = 50; //former:1000

                int period = 25;// former:500 循环间隔

                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (dfsGrid[rows - 1][cols - 1].isPersonCome()) {
                            endTime = System.currentTimeMillis();
                            JOptionPane.showMessageDialog(null, "已经走出迷宫，耗时"
                                            + (endTime - startTime) / 1000 + "秒", "消息提示",
                                    JOptionPane.ERROR_MESSAGE);
                            centerPanel.setVisible(false);
                            dfsGrid[0][0].setVisited(true);
                            dfsGrid[rows - 1][cols - 1].setPersonCome(false);
                            dfsGrid[0][0].setPersonCome(true);
                            dfsGrid[0][0].setStart(true);

                            comeX = 0;
                            comeY = 0;
                            willVisit.clear();
                            visited.clear();
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

                int delay = 50; //former:1000

                int period = 25;// former:500 循环间隔

                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (primGrid[rows - 1][cols - 1].isPersonCome()) {
                            endTime = System.currentTimeMillis();
                            JOptionPane.showMessageDialog(null, "已经走出迷宫，耗时"
                                            + (endTime - startTime) / 1000 + "秒", "消息提示",
                                    JOptionPane.ERROR_MESSAGE);

                            primGrid[0][0].setVisited(true);
                            primGrid[rows - 1][cols - 1].setPersonCome(false);
                            primGrid[0][0].setPersonCome(true);
                            primGrid[0][0].setStart(true);

                            comeX = 0;
                            comeY = 0;
                            willVisit.clear();
                            visited.clear();
                            comed.clear();

                            dfsReStart.setVisible(true);
                            dfsStart.setVisible(true);
                            primReStart.setText("regenerate the primMaze");
                            primReStart.setVisible(true);
                            primStart.setVisible(true);
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
                int delay = 50; //former:1000

                int period = 25;// former:500 循环间隔

                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (ellerGrid[rows - 1][cols - 1].isPersonCome()) {
                            endTime = System.currentTimeMillis();
                            JOptionPane.showMessageDialog(null, "已经走出迷宫，耗时"
                                            + (endTime - startTime) / 1000 + "秒", "消息提示",
                                    JOptionPane.ERROR_MESSAGE);

                            ellerGrid[0][0].setVisited(true);
                            ellerGrid[rows - 1][cols - 1].setPersonCome(false);
                            ellerGrid[0][0].setPersonCome(true);
                            ellerGrid[0][0].setStart(true);

                            comeX = 0;
                            comeY = 0;
                            willVisit.clear();
                            visited.clear();
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
        visited.clear();
        comed.clear();
        wallList.clear();

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
        Maze maze = new Maze();
        long end = System.currentTimeMillis();
        System.out.println("使用ArrayList生成迷宫耗时：" + (end - start) + "毫秒");
    }
}
