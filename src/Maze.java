import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// 迷宫
@SuppressWarnings("serial")
public class Maze extends JFrame implements ActionListener {

    private JPanel panel;
    private JPanel controlPanel;//control按钮面板
    private JPanel centerPanel; //主区域面板
    private JPanel dfsCenterPanel;
    private JPanel primCenterPanel;
    private JPanel ellerCenterPanel;
    private MazeGrid dfsGrid[][];
    private MazeGrid primGrid[][];
    private MazeGrid ellerGrid[][];
    private JButton dfsReStart;
    private JButton dfsStart;
    private JButton primReStart;
    private JButton primStart;

    private int rows;// rows 和cols目前暂定只能是奇数
    private int cols;
    private List<String> willVisit; //保存待访问的当前块的邻接块
    private List<String> visited;   //保存图中所有已访问过块
    private LinkedList<String> comed;
    private List<String> wallList; //prim算法墙链
    private long startTime;
    private long endTime;

    public Maze() {
        rows = 25; //定义迷宫大小
        cols = 25;
        willVisit = new ArrayList<String>();
        visited = new ArrayList<String>();
        comed = new LinkedList<String>();
        wallList = new LinkedList<String>();
        init();
        this.setTitle("回溯法--走迷宫");
        this.add(panel);
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() {
        panel = new JPanel();
        controlPanel = new JPanel();
        centerPanel = new JPanel();
        dfsCenterPanel = new JPanel();
        primCenterPanel = new JPanel();
        panel.setLayout(new BorderLayout()); //方位布局
        dfsReStart = new JButton("regenerate the dfsMaze");//dfs迷宫 restart按钮
        dfsStart = new JButton("dfsStart to explore");
        primReStart = new JButton("regenerate the primMaze");
        primStart = new JButton("primStart to explore");

        primGrid = new MazeGrid[rows][cols];
        dfsGrid = new MazeGrid[rows][cols];
        ellerGrid = new MazeGrid[rows][cols];
        /*原来部分*/
//        centerPanel.setLayout(new GridLayout(rows, cols, 1, 1)); //网格布局
//        centerPanel.setBackground(new Color(0, 0, 0));
        /*原来部分*/

        centerPanel.setLayout(new GridLayout(1, 2, 40, 0));//两个迷宫间距水平：40 纵向: 0
        centerPanel.add(dfsCenterPanel);
        centerPanel.add(primCenterPanel);

        //dfs迷宫布局
        dfsCenterPanel.setLayout(new GridLayout(rows, cols, 1, 1));//dfs迷宫 网格式布局
        dfsCenterPanel.setBackground(new Color(0, 0, 0));

        //prim迷宫布局
        primCenterPanel.setLayout(new GridLayout(rows, cols, 1, 1));
        primCenterPanel.setBackground(new Color(0, 0, 0));

        controlPanel.setLayout(new GridLayout(1, 4, 40, 0));
        controlPanel.add(dfsReStart); //添加dfs restart button
        controlPanel.add(dfsStart); //添加dfs start button
        controlPanel.add(primReStart);//添加prim restart button
        controlPanel.add(primStart);//添加prim start button

        panel.add(controlPanel, BorderLayout.NORTH); //上方加入控制面板
        panel.add(centerPanel, BorderLayout.CENTER); //中心放置迷宫


        dfsStart.addActionListener(this);
        dfsReStart.addActionListener(this);
//        primStart.addActionListener(this);
//        primReStart.addActionListener(this);


        //初始化迷宫图的各单元
        for (int i = 0; i < dfsGrid.length; i++)
            for (int j = 0; j < dfsGrid[i].length; j++) {
                //mark=true(偶数坐标)为通路, false(奇数坐标)为墙
                if (j % 2 == 0 && i % 2 == 0) {
                    dfsGrid[i][j] = new MazeGrid(true, 20, 20);
                    primGrid[i][j] = new MazeGrid(true, 20, 20);//初始化prim
                    ellerGrid[i][j] = new MazeGrid(true, 20, 20);
                } else {
                    dfsGrid[i][j] = new MazeGrid(false, 20, 20);
                    primGrid[i][j] = new MazeGrid(false, 20, 20);//初始化prim
                    ellerGrid[i][j] = new MazeGrid(false, 20, 20);
                }
            }

        //初始化入口
        dfsGrid[0][0].setVisited(true);
        dfsGrid[0][0].setPersonCome(true);
        dfsGrid[0][0].setStart(true);

        primGrid[0][0].setVisited(true);
        primGrid[0][0].setPersonCome(true);
        primGrid[0][0].setStart(true);

        ellerGrid[0][0].setVisited(true);
        ellerGrid[0][0].setPersonCome(true);
        ellerGrid[0][0].setStart(true);

        visited.add("0#0");

        //初始化出口
        dfsGrid[rows - 1][cols - 1].setEnd(true);

        primGrid[rows - 1][cols - 1].setEnd(true);

        ellerGrid[rows - 1][cols - 1].setEnd(true);

        //从grid[0][0]开始递归生成迷宫

//        /*DFS生成迷宫*/
        createDfsMap dfsMap = new createDfsMap();//生成dfs对象
        dfsMap.SetProperty(rows, cols, willVisit, visited, dfsGrid);//把属性传入
        dfsGrid = dfsMap.generateMap();//生成迷宫
        /*DFS生成迷宫*/

        /*Prim生成迷宫*/
//        creatPrimMap primMap = new creatPrimMap();
//        primMap.SetProperty(rows, cols, wallList, dfsGrid);
//        primGrid = primMap.generateMap();
        /*Prim生成迷宫*/

//        /*Eller生成迷宫*/
//        CreateEllerMap ellerMap = new CreateEllerMap();
//        ellerMap.SetProperty(rows,cols,willVisit,visited,dfsGrid);
//        dfsGrid = ellerMap.generateMap();
//        /*Eller生成迷宫*/



        /*dfs迷宫打印*/
        for (MazeGrid[] mazeGrids : dfsGrid) { //foreach 以每行遍历
            for (int j = 0; j < mazeGrids.length; j++) { //依次遍历每行各个方块

                mazeGrids[j].repaint();
                dfsCenterPanel.add(mazeGrids[j]);
                /*原先*/
//                centerPanel.add(mazeGrids[j]);
            }
        }

        /*prim迷宫打印*/
//        for (MazeGrid[] mazeGrids : primGrid) { //foreach 以每行遍历
//            for (int j = 0; j < mazeGrids.length; j++) { //依次遍历每行各个方块
//
//                mazeGrids[j].repaint();
//                primCenterPanel.add(mazeGrids[j]);
//                /*原先*/
////                centerPanel.add(mazeGrids[j]);
//            }
//        }

//        for (MazeGrid[] mazeGrids : ellerGrid) { //foreach 以每行遍历
//            for (int j = 0; j < mazeGrids.length; j++) { //依次遍历每行各个方块
//
//                mazeGrids[j].repaint();
//                primCenterPanel.add(mazeGrids[j]);
//                /*原先*/
////                centerPanel.add(mazeGrids[j]);
//            }
//        }


    }

//    /**
//     * 生成迷宫
//     */
//    public MazeGrid[][] createMap(MazeGrid mazeGrid[][], int x, int y) {
//        int visitX = 0;
//        int visitY = 0;
//        if (x - 2 >= 0) {
//            if (!mazeGrid[x - 2][y].isVisited()) { //左侧方块未visit
//                willVisit.add((x - 2) + "#" + y);
//            }
//        }
//        if (x + 2 < cols) {
//            if (!mazeGrid[x + 2][y].isVisited()) { //右侧方块未visit
//                willVisit.add((x + 2) + "#" + y);
//            }
//        }
//        if (y - 2 >= 0) {
//            if (!mazeGrid[x][y - 2].isVisited()) { //下侧方块未visit
//                willVisit.add(x + "#" + (y - 2));
//            }
//        }
//        if (y + 2 < rows) {
//            if (!mazeGrid[x][y + 2].isVisited()) { //上侧方块未visit
//                willVisit.add(x + "#" + (y + 2));
//            }
//        }
//        if (!willVisit.isEmpty()) { //如果willVisit列表不为空(有邻接点访问)
//            int visit = (int) (Math.random() * willVisit.size()); //产生0~size的随机数
//            String id = willVisit.get(visit);                     //根据随机数获取列表中随机的一个相邻块
//            visitX = Integer.parseInt(id.split("#")[0]);   //根据"#"分割字符串，将x,y坐标获取
//            visitY = Integer.parseInt(id.split("#")[1]);   //将获取的x,y(string类型)转成int类型，并赋给visitX/Y
//            mazeGrid[(visitX + x) / 2][(visitY + y) / 2].setMark(true); //将当前块与邻接块中间的块(墙)设为通路
//
//            mazeGrid[visitX][visitY].setVisited(true); //标记该邻接块已visit过
//            if (!visited.contains(id)) {// 将这个方块加到已访问中去
//                visited.add(id);
//            }
//            willVisit.clear();  //将邻接点列表清空
//            createMap(mazeGrid, visitX, visitY); //递归调用，下次起点从mazeGrid[visitX][visitY]开始
//        } else {
//            if (!visited.isEmpty()) {   //无邻接点访问，返回上一个访问过的方块
//                String id = visited.remove(visited.size() - 1);// 取出最后一个元素(退回上一个访问过的方块)
//                visitX = Integer.parseInt(id.split("#")[0]);
//                visitY = Integer.parseInt(id.split("#")[1]);
//                mazeGrid[visitX][visitY].setVisited(true);
//                createMap(mazeGrid, visitX, visitY); //递归调用，下次起点从退回的(上一个)方块开始
//            }
//        }
//        return mazeGrid;
//    }

    /**
     * 走迷宫
     */
    public String goMaze(MazeGrid mazeGrid[][], int x, int y) {
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

    int comeX = 0;
    int comeY = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("regenerate the dfsMaze")) {
            long start = System.currentTimeMillis();
            refreshMap(dfsGrid);
            long end = System.currentTimeMillis();
            System.out.println("使用ArrayList生成迷宫耗时：" + (end - start) + "毫秒");
        } else if (e.getActionCommand().equals("dfsStart to explore")) {
            startTime = System.currentTimeMillis();
            dfsStart.setVisible(false);
            dfsReStart.setText("prohibit to refresh");
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
                        this.cancel();
                        dfsReStart.setText("regenerate the dfsMaze");
                    } else {
                        String id = goMaze(dfsGrid, comeX, comeY);
                        comeX = Integer.parseInt(id.split("#")[0]);
                        comeY = Integer.parseInt(id.split("#")[1]);
                    }
                }
            }, delay, period);
        }
    }

    /**
     * 刷新地图
     */
    public void refreshMap(MazeGrid mazeGrid[][]) {
        comeX = 0;
        comeY = 0;
        willVisit.clear();
        visited.clear();
        comed.clear();
        this.remove(panel);
        init();
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String args[]) {
        long start = System.currentTimeMillis();
        new Maze();
        long end = System.currentTimeMillis();
        System.out.println("使用ArrayList生成迷宫耗时：" + (end - start) + "毫秒");
    }
}
