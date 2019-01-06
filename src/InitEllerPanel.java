import javax.swing.*;
import java.awt.*;

class InitEllerPanel {

    private MazeGrid ellerGrid[][];

    private JPanel mazePanel;

    InitEllerPanel(int rows, int cols) {
        ellerGrid = new MazeGrid[rows][cols];

        mazePanel = new JPanel();
        mazePanel.setLayout(new GridLayout(rows, cols, 1, 1));
        mazePanel.setBackground(new Color(240, 240, 240));

        for (int i = 0; i < ellerGrid.length; i++)
            for (int j = 0; j < ellerGrid[i].length; j++) {
                if (j % 2 == 0 && i % 2 == 0) {
                    ellerGrid[i][j] = new MazeGrid(true, 20, 20);
                } else {
                    ellerGrid[i][j] = new MazeGrid(false, 20, 20);
                }
            }

        ellerGrid[0][0].setVisited();
        ellerGrid[0][0].setPersonCome(true);
        ellerGrid[0][0].setStart();

//        List<String> visited = new ArrayList<>();
//        visited.add("0#0");

        ellerGrid[rows - 1][cols - 1].setEnd();

        /*Eller生成迷宫*/
        CreateEllerMap ellerMap = new CreateEllerMap();
        ellerMap.SetProperty(rows, ellerGrid);
        ellerGrid = ellerMap.generateMap();
        /*Eller生成迷宫*/

        /*eller迷宫打印*/
        for (MazeGrid[] mazeGrids : ellerGrid) { //foreach 以每行遍历
            for (MazeGrid mazeGrid : mazeGrids) { //依次遍历每行各个方块

                mazeGrid.repaint();
                mazePanel.add(mazeGrid);
            }
        }

    }

    MazeGrid[][] getMaze() {
        return this.ellerGrid;
    }

    JPanel getMazePanel() {
        return this.mazePanel;
    }
}
