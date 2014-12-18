package mod6;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class GuiDialog extends JDialog {
	private static final long serialVersionUID = 7655733541636157410L;

	public String lastChoice;
	private static JLabel label;

	public GuiDialog(JFrame owner) {
		super(owner, "Select the actual class");
		JPanel panel = new JPanel(new MigLayout());
		this.setLocation(500, 230);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		label = new JLabel("What is your gender?");
		label.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnFemale = new JButton("Female");
		btnFemale.setBackground(Color.PINK);
		JButton btnMale = new JButton("Male");
		btnMale.setBackground(Color.BLUE);

		btnFemale.addActionListener(new ButtonListener(this));
		btnMale.addActionListener(new ButtonListener(this));
		
		panel.add(label, "w 100%, span 2, wrap");
		panel.add(btnFemale, "w 50%");
		panel.add(btnMale, "w 50%, wrap");

		this.add(panel);
		this.pack();
	}

	public static class ButtonListener implements ActionListener {

		GuiDialog dialog;

		public ButtonListener(GuiDialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dialog.lastChoice = ((JButton) e.getSource()).getText();
			dialog.setVisible(false);
		}

	}

}
