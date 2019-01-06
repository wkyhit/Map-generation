import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateEllerMap {
    private Random ran = new Random(System.currentTimeMillis());

    private MazeGrid grid[][];
    private int size;


    void SetProperty(int rows, MazeGrid grid[][]) {
        this.grid = grid;
        size = (rows + 1) / 2;
    }

    MazeGrid[][] generateMap() {
        return createMap(grid, 0, 0);
    }


    private MazeGrid[][] createMap(MazeGrid mazeGrid[][], int x, int y) {
        int[][] check = new int[2][size];
        HashMap<Integer, Boolean> downMap = new HashMap<>();
        HashMap<Integer, Integer> downMapIndex = new HashMap<>();
        int index = 0;
        int area = 0;
        while (index < size) {
            // 判断格子是否同一区域，随机打通格子
            // 最后一行需要打通所有区域
            for (int i = 0; i < size - 1; i++) {
                int a = check[index % 2][i];
                int b = check[index % 2][i + 1];

                // 两个格子属于同一区域
                if (a != 0 && b != 0 && a == b) {
                    continue;
                }

                // 0：破墙，1：不破墙
                if (ran.nextInt(2) == 0) {
                    mazeGrid[(index << 1)][(i << 1) + 1].setMark();     //i<<1表示i*2,表示方法不同但是效率更高
                    if (a == 0) {
                        check[index % 2][i] = ++area;
                    }
                    check[index % 2][i + 1] = check[index % 2][i];
                } else {
                    // 该格子与其他区域不连通，作为单独一个区域
                    if (index == size - 1) {
                        mazeGrid[(index << 1)][(i << 1) + 1].setMark();
                    } else if (b == 0) {
                        check[index % 2][i + 1] = ++area;
                    }
                }
            }

            // 向下破墙，每个区域至少有一个格子打通下一层
            for (int i = 0; i < size; i++) {
                if (ran.nextInt(2) == 0) {
                    try {
                        // 破墙，将格子合并到区域
                        mazeGrid[index * 2 + 1][i * 2].setMark();
                        check[(index + 1) % 2][i] = check[index % 2][i];
                        downMap.put(check[index % 2][i], true);
                    } catch (Exception ignored) {

                    }
                } else {
                    // 不破墙，记录该区域未连通下行时的最后一个格子
                    check[(index + 1) % 2][i] = 0;
                    if (!downMap.containsKey(check[index % 2][i]) || !downMap.get(check[index % 2][i])) {
                        downMap.put(check[index % 2][i], false);
                        downMapIndex.put(check[index % 2][i], i);
                    }
                }
            }

            // 打通未与下一层连通的区域
            for (Map.Entry<Integer, Boolean> entity : downMap.entrySet()) {
                if (!entity.getValue()) {
                    int col = downMapIndex.get(entity.getKey());
                    try {
                        mazeGrid[index * 2 + 1][col * 2].setMark();
                        check[(index + 1) % 2][col] = check[index % 2][col];
                    } catch (Exception ignored) {

                    }
                }
            }

            downMap.clear();
            downMapIndex.clear();

            index++;
        }


        return mazeGrid;
    }
}