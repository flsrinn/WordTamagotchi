import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class Ranking {
    private FileWriter fout;
    private Vector<String> easyNames = new Vector<String>();
    private Vector<String> easyScores = new Vector<String>();
    private Vector<String> hardNames = new Vector<String>();
    private Vector<String> hardScores = new Vector<String>();
    private Vector<String> easyRank = new Vector<String>();
    private Vector<String> hardRank = new Vector<String>();
    private Scanner easyList;
    private Scanner hardList;

    // 텍스트 파일에 저장되어 있는 정보를 벡터에 저장하기
    public Ranking() {
	try {
	    easyList = new Scanner(new FileReader("text/rank/easyRank.txt"));
	    while (easyList.hasNext()) {
		easyNames.add(easyList.nextLine());
		easyScores.add(easyList.nextLine());
	    }
	    hardList = new Scanner(new FileReader("text/rank/hardRank.txt"));
	    while (hardList.hasNext()) {
		hardNames.add(hardList.nextLine());
		hardScores.add(hardList.nextLine());
	    }
	} catch (FileNotFoundException e) {
	    JOptionPane.showMessageDialog(null, "파일 없음");
	    return;
	}
	rank();
    }

    // 텍스트 파일에 랭킹 정보 추가하기
    public void addRanking(int level, String playerName, int score) {
	if (level == 1) {
	    try {
		fout = new FileWriter("text/rank/easyRank.txt", true);
		fout.write(playerName + "\n" + score + "\n");
		fout.close();
	    } catch (IOException e) {
		JOptionPane.showMessageDialog(null, "파일 없음");
	    }
	} else {
	    try {
		fout = new FileWriter("text/rank/hardRank.txt", true);
		fout.write(playerName + "\n" + score + "\n");
		fout.close();
	    } catch (IOException e) {
		JOptionPane.showMessageDialog(null, "파일 없음");
	    }
	}
    }

    // top 5 정렬
    private void rank() {
	int max, maxIndex, count = 0;
	boolean alreadyExists;
	easyRank.clear();
	hardRank.clear();
	while (true) {
	    alreadyExists = false;
	    max = 0;
	    maxIndex = 0;
	    // 등록된 기록이 5개 미만일 경우
	    if (easyNames.size() == 0)
		break;
	    else if (count == 5)
		break;
	    for (int i = 0; i < easyNames.size(); i++) {
		int score = Integer.parseInt(easyScores.get(i));
		if (max < score) {
		    max = score;
		    maxIndex = i;
		}
	    }

	    // 같은 이름이 랭킹에 존재하는지 확인
	    for (int i = 0; i < easyRank.size(); i++) {
		String info = easyRank.get(i);
		StringTokenizer st = new StringTokenizer(info, ",");
		String rankName = st.nextToken();
		if (easyNames.get(maxIndex).equals(rankName)) {
		    alreadyExists = true;
		}
	    }

	    // 같은 이름이 존재하지 않을 경우에만 top 5 랭킹에 추가
	    if (!alreadyExists) {
		count++;
		easyRank.add(easyNames.get(maxIndex) + "," + Integer.toString(max));
	    }

	    easyNames.remove(maxIndex);
	    easyScores.remove(maxIndex);
	}
	count = 0;
	while (true) {
	    alreadyExists = false;
	    max = 0;
	    maxIndex = 0;
	    // 등록된 기록이 5개 미만일 경우
	    if (hardNames.size() == 0)
		break;
	    else if (count == 5)
		break;
	    for (int i = 0; i < hardNames.size(); i++) {
		int score = Integer.parseInt(hardScores.get(i));
		if (max < score) {
		    max = score;
		    maxIndex = i;
		}
	    }

	    // 같은 이름이 랭킹에 존재하는지 확인
	    for (int i = 0; i < hardRank.size(); i++) {
		String info = hardRank.get(i);
		StringTokenizer st = new StringTokenizer(info, ",");
		String rankName = st.nextToken();
		if (hardNames.get(maxIndex).equals(rankName)) {
		    alreadyExists = true;
		}
	    }

	    // 같은 이름이 존재하지 않을 경우에만 top 5 랭킹에 추가
	    if (!alreadyExists) {
		count++;
		hardRank.add(hardNames.get(maxIndex) + "," + Integer.toString(max));
	    }

	    hardNames.remove(maxIndex);
	    hardScores.remove(maxIndex);
	}

    }

    // top 5 정보를 저장한 벡터 리턴하기
    public Vector<String> getRank(int level) {
	if (level == 1)
	    return easyRank;
	else
	    return hardRank;
    }
}
