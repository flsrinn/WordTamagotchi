import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class TextSource {
    private Vector<String> wordVector = new Vector<String>(30000);
    private FileWriter fout;

    // 텍스트 파일로부터 단어를 읽어와 벡터에 저장
    public TextSource(Component parent) {
	try {
	    Scanner scanner = new Scanner(new FileReader("text/words.txt"));
	    while (scanner.hasNext()) {
		String word = scanner.nextLine();
		wordVector.add(word);
	    }
	    scanner.close();
	} catch (FileNotFoundException e) {
	    JOptionPane.showMessageDialog(null, "파일 없음");
	    System.exit(0);
	}
    }

    // 벡터에서 랜덤 인덱스의 단어를 리턴하는 메소드
    public String next() {
	int n = wordVector.size();
	int index = (int) (Math.random() * n);
	return wordVector.get(index);
    }

    // 이미 존재하는 단어인지 찾는 메소드
    // 존재하면 true, 없으면 false 리턴
    public boolean search(String word) {
	for (int i = 0; i < wordVector.size(); i++) {
	    if (wordVector.get(i).equals(word))
		return true;
	}
	return false;
    }

    // 텍스트 파일에 단어를 추가하는 메소드
    public void add(String word) {
	try {
	    fout = new FileWriter("text/words.txt", true);
	    fout.write(word);
	    fout.write("\n");
	    fout.close();
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "파일 없음");
	}

	wordVector.add(word);
    }
}
