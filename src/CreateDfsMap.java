import java.util.List;


class CreateDfsMap {

    private int rows;// rows 和cols只能是奇数
    private int cols;
    private List<String> willVisit; //保存待访问的当前方格的相邻方格
    private List<String> visited;   //保存图中所有已访问过方格
    private MazeGrid grid[][];


    CreateDfsMap() {
    }

    void SetProperty(int rows, int cols, List<String> willVisit, List<String> visited, MazeGrid grid[][]) {
        this.rows = rows;
        this.cols = cols;
        this.willVisit = willVisit;
        this.visited = visited;
        this.grid = grid;
    }

    MazeGrid[][] generateMap() {
        return createMap(grid, 0, 0);
    }


    private MazeGrid[][] createMap(MazeGrid mazeGrid[][], int x, int y) {

        int visitX;
        int visitY;
        if (x - 2 >= 0) {
            if (mazeGrid[x - 2][y].noVisited()) { //左侧方块未visit
                willVisit.add((x - 2) + "#" + y);
            }
        }
        if (x + 2 < cols) {
            if (mazeGrid[x + 2][y].noVisited()) { //右侧方块未visit
                willVisit.add((x + 2) + "#" + y);
            }
        }
        if (y - 2 >= 0) {
            if (mazeGrid[x][y - 2].noVisited()) { //下侧方块未visit
                willVisit.add(x + "#" + (y - 2));
            }
        }
        if (y + 2 < rows) {
            if (mazeGrid[x][y + 2].noVisited()) { //上侧方块未visit
                willVisit.add(x + "#" + (y + 2));
            }
        }
        if (!willVisit.isEmpty()) { //如果willVisit列表不为空(有邻接点访问)
            int visit = (int) (Math.random() * willVisit.size()); //产生0~size的随机数
            String id = willVisit.get(visit);                     //根据随机数获取列表中随机的一个相邻块
            visitX = Integer.parseInt(id.split("#")[0]);   //根据"#"分割字符串，将x,y坐标获取
            visitY = Integer.parseInt(id.split("#")[1]);   //将获取的x,y(string类型)转成int类型，并赋给visitX/Y
            mazeGrid[(visitX + x) / 2][(visitY + y) / 2].setMark(); //将当前块与邻接块中间的块(墙)设为通路

            mazeGrid[visitX][visitY].setVisited(); //标记该邻接块已visit过
            if (!visited.contains(id)) {// 将这个方块加到已访问中去
                visited.add(id);
            }
            willVisit.clear();  //将邻接点列表清空
            createMap(mazeGrid, visitX, visitY); //递归调用，下次起点从mazeGrid[visitX][visitY]开始
        } else {
            if (!visited.isEmpty()) {   //无邻接点访问，返回上一个访问过的方块
                String id = visited.remove(visited.size() - 1);// 取出最后一个元素(退回上一个访问过的方块)
                visitX = Integer.parseInt(id.split("#")[0]);
                visitY = Integer.parseInt(id.split("#")[1]);
                mazeGrid[visitX][visitY].setVisited();
                createMap(mazeGrid, visitX, visitY); //递归调用，下次起点从退回的(上一个)方块开始
            }
        }
        return mazeGrid;
    }
}
