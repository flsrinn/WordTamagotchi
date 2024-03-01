import java.awt.*;

import javax.swing.*;

public class MonsterPanel extends JPanel {
    private ImageIcon background = new ImageIcon("image/dama.png");
    private Image backgroundImg = background.getImage();
    private ImageIcon babyImg[] = new ImageIcon[3];
    private ImageIcon adultImg[] = new ImageIcon[3];
    private ImageIcon monster;
    private JLabel monsterImg;
    private int stage;
    public MonsterThread mt;

    public MonsterPanel(int stage, int monsterIndex) {
	setLayout(null);
	this.stage = stage;
	// 스테이지 1일 경우 플레이어가 선택한 알 사진 저장
	if (stage == 1) {
	    monster = new ImageIcon("image/monster/egg" + (monsterIndex + 1) + ".png");
	    monsterImg = new JLabel(new ImageIcon(monster.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
	}

	// 스테이지 2일 경우 선택한 알의 유년기 사진 저장
	else if (stage == 2) {
	    babyImg[0] = new ImageIcon("image/monster/baby" + (monsterIndex + 1) + ".png");
	    babyImg[1] = new ImageIcon("image/monster/happybaby" + (monsterIndex + 1) + ".png");
	    babyImg[2] = new ImageIcon("image/monster/sadbaby" + (monsterIndex + 1) + ".png");

	    monsterImg = new JLabel(
		    new ImageIcon(babyImg[0].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
	}

	// 스테이지 3 또는 추가 스테이지일 경우 
	// 선택한 알의 성년기 사진 저장
	else {
	    adultImg[0] = new ImageIcon("image/monster/adult" + (monsterIndex + 1) + ".png");
	    adultImg[1] = new ImageIcon("image/monster/happyadult" + (monsterIndex + 1) + ".png");
	    adultImg[2] = new ImageIcon("image/monster/sadadult" + (monsterIndex + 1) + ".png");
	    monsterImg = new JLabel(
		    new ImageIcon(adultImg[0].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
	}

	monsterImg.setLocation(65, 50);
	monsterImg.setSize(100, 100);
	add(monsterImg);

	mt = new MonsterThread(stage);
	mt.start();
    }

    // 몬스터의 이미지를 바꾸는 메세지
    public void changeImg(int index) {
	// 스테이지 2일 경우 유년기 사진 바꾸기
	if (stage == 2) {
	    monsterImg
		    .setIcon(new ImageIcon(babyImg[index].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
	}
	// 스테이지 3 또는 추가 스테이지일 경우 성년기 사진 바꾸기
	else if (stage >= 3) {
	    monsterImg
		    .setIcon(new ImageIcon(adultImg[index].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
	}
	// 인덱스가 1일 경우(맞았을 경우)
	// 몬스터가 움직이게 함
	if (index == 1) {
	    mt.startMove();
	}
	// 인덱스가 0이거나(점수 증가x) 2일 경우(틀렸을 경우)
	// 몬스터 움직임을 멈춤
	else if (index == 0 || index == 2) {
	    mt.stopMove();
	}
	monsterImg.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	setBackground(new Color(14, 13, 89));
	g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), this);
	g.setColor(Color.white);
	g.fillRect(65, 43, 103, 84);
    }

    // 몬스터의 움직임을 구현하는 메소드
    class MonsterThread extends Thread {
	private boolean stopFlag = true;
	private int stage;
	private int count;

	public MonsterThread(int stage) {
	    this.stage = stage;
	}

	// 몬스터가 움직이게 하는 메소드
	synchronized public void startMove() {
	    stopFlag = false;
	    notify();
	}

	// 몬스터의 움직임을 멈추는 메소드
	public void stopMove() {
	    stopFlag = true;
	}

	// stopFlag가 true일 경우
	// 스레드를 wait 상태로 만드는 메소드
	synchronized private void checkWait() {
	    if (stopFlag) {
		try {
		    count = 0;
		    if (stage == 1) {
			monsterImg.setLocation(65, monsterImg.getY());
		    } else {
			monsterImg.setLocation(monsterImg.getX(), 50);
		    }
		    wait();
		} catch (InterruptedException e) {
		    return;
		}
	    }
	}

	@Override
	public void run() {
	    count = 0;
	    while (true) {
		checkWait();
		if (!stopFlag) {
		    // 스테이지 1일 경우 좌우로 이동
		    if (stage == 1) {
			if (count % 2 == 0) {
			    monsterImg.setLocation(60, monsterImg.getY());
			} else {
			    monsterImg.setLocation(70, monsterImg.getY());
			}
		    }

		    // 스테이지 2 이상일 경우 위아래로 이동
		    else if (stage >= 2) {
			if (count % 2 == 0) {
			    monsterImg.setLocation(monsterImg.getX(), monsterImg.getY() - 10);
			} else {
			    monsterImg.setLocation(monsterImg.getX(), monsterImg.getY() + 10);
			}
		    }
		    count++;
		    try {
			sleep(300);
		    } catch (InterruptedException e) {
			stopFlag = true;
			return;
		    }
		}
	    }

	}
    }
}
