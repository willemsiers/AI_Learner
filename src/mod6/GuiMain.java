package mod6;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;

public class GuiMain {

	private JButton btnClassify;
	private JTextField tfInput;
	private JTextArea taLog;
	private JScrollPane spLog;
	private GuiDialog guiDialog;

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException, IOException {
		Learner learner = new Learner(new Model("M", "male"), new Model("F", "female"));
		GuiMain guiMain = new GuiMain();
		guiMain.setLearner(learner);
	}

	private GuiMain() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(400, 400);
		frame.setTitle("Interactive Learner");
		frame.setLocation(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new MigLayout());
		frame.setMinimumSize(new Dimension(500, 250));

		tfInput = new JTextField("", 40);
		tfInput.setToolTipText("Enter text to be classified.");

		taLog = new JTextArea(8, 40);
		spLog = new JScrollPane(taLog);
		spLog.setMinimumSize(new Dimension(50, 150));
		taLog.setEditable(false);
		taLog.append("Welcome to the interactive learner.\nWrite text below to begin.\nI will tell you whether the writer is male or female.\n");
		taLog.append("_________________________________________________________\n");
		btnClassify = new JButton("Classify!");

		panel.add(spLog, "w 100%, h 75%, span 2, wrap");
		panel.add(tfInput, "w 100%, span 2, wrap");
		panel.add(btnClassify, "w 100%, wrap");

		frame.add(panel);
		frame.pack();

		frame.getRootPane().setDefaultButton(btnClassify);

		guiDialog = new GuiDialog(frame);
		guiDialog.setModal(true);
	}

	public void appendLog(String text) {
		JScrollBar vertical = this.spLog.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		taLog.append(text);
	}

	public void setLearner(Learner learner) {
		ClassifyListener onClassify = new ClassifyListener(learner, this);
		btnClassify.addActionListener(onClassify);
	}

	public static class ClassifyListener implements ActionListener {

		private Learner learner;
		private GuiMain gui;

		public ClassifyListener(Learner learner, GuiMain gui) {
			this.learner = learner;
			this.gui = gui;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Classification classification = learner.classifyString(gui.tfInput.getText());
			gui.guiDialog.setVisible(true);

			Model userinput = (gui.guiDialog.lastChoice.equalsIgnoreCase(learner.maleModel.name)) ? learner.maleModel
					: learner.femaleModel;

			String[] words = classification.words;
			
			if (userinput == classification.model) {
				if (words.length > 3) {
					gui.appendLog(String.format("\u2713 \"%s...%s\"\tcorrect %s\n", words[0], words[words.length - 1], classification.toString()));
				} else {
					gui.appendLog(String.format("\u2713 \"%s\"\tcorrect %s\n", gui.tfInput.getText(), classification.toString()));
				}
			} else {
				if (words.length > 3) {
					gui.appendLog(String.format("\u2717 \"%s...%s\"\tlooked %s\n", words[0], words[words.length - 1], classification.toString()));
				} else {
					gui.appendLog(String.format("\u2717 \"%s\"\tlooked %s\n", gui.tfInput.getText(), classification.toString()));
				}
				classification.model = userinput;
			}
			learner.addClassification(classification);
		}

	}

}
