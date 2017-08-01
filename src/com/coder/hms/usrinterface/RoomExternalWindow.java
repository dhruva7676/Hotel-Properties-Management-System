/**
 * @author Coder ACJHP
 * @Email hexa.octabin@gmail.com
 * @Date 15/07/2017
 */
package com.coder.hms.usrinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import com.coder.hms.daoImpl.CustomerDaoImpl;
import com.coder.hms.daoImpl.PaymentDaoImpl;
import com.coder.hms.daoImpl.PostingDaoImpl;
import com.coder.hms.daoImpl.ReservationDaoImpl;
import com.coder.hms.daoImpl.RoomDaoImpl;
import com.coder.hms.entities.Customer;
import com.coder.hms.entities.Payment;
import com.coder.hms.entities.Posting;
import com.coder.hms.entities.Reservation;
import com.coder.hms.entities.Room;
import com.coder.hms.utils.ApplicationLogoSetter;
import com.coder.hms.utils.PayPostTableCellRenderer;
import com.coder.hms.utils.RoomExternalTableHeaderRenderer;
import com.toedter.calendar.JDateChooser;

public class RoomExternalWindow extends JDialog {

	/**
	 * 
	 */
	private static String roomNumber;
	private JTextPane roomNote;
	private NumberFormat formatter;
	private JTable payPostTable, customerTable;
	private JDateChooser checkinDate, checkoutDate;
	private static final long serialVersionUID = 1L;
	private JButton postingBtn, paymentBtn, saveChangesBtn, checkoutBtn;
	private JFormattedTextField priceField,totalPriceField, balanceField;
	private final String LOGOPATH = "/com/coder/hms/icons/main_logo(128X12).png";
	private final ApplicationLogoSetter logoSetter = new ApplicationLogoSetter();
	private final PayPostTableCellRenderer payPostRenderer = new PayPostTableCellRenderer();
	private final RoomExternalTableHeaderRenderer THR = new RoomExternalTableHeaderRenderer();
	
	private final String[] customerColnames = new String[]{"INDEX", "FIRSTNAME", "LASTNAME"};
	private final DefaultTableModel customerModel = new DefaultTableModel(customerColnames, 0);
	
	private final String[] postPayColnames = new String[]{"DOC. NO", "TYPE", "TITLE", "PRICE", "CURRENCY", "EXPLANATION", "DATE TIME"};
	private final DefaultTableModel postPayModel = new DefaultTableModel(postPayColnames, 0);
	private JTextField IdField, groupNameField, agencyField, currencyField, creditField, hostTypeField, totalDaysField;


	/**
	 * Create the dialog.
	 * @param roomText 
	 */
	public RoomExternalWindow(String roomText) {
		
		RoomExternalWindow.roomNumber = roomText;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setMinimumSize(new Dimension(1000, 700));
		setSize(new Dimension(1121, 700));
		setPreferredSize(new Dimension(1000, 700));
		// set upper icon for dialog frame
		logoSetter.setApplicationLogoJDialog(this, LOGOPATH);

		getContentPane().setForeground(new Color(255, 99, 71));
		getContentPane().setFocusCycleRoot(true);
		getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		getContentPane().setFont(new Font("Verdana", Font.BOLD, 12));
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModal(true);
		setResizable(false);

		this.setTitle("Coder for HMS - [RoomEx] : " + roomText);

		/* Set default size of frame */
		final Dimension computerScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		String opSystem = System.getProperty("os.name").toLowerCase();

		if (opSystem.contains("windows") || opSystem.contains("nux")) {
			
			this.setSize(computerScreenSize);
		}else {
			
			final Dimension wantedRoomFrameSize = new Dimension(computerScreenSize.width, computerScreenSize.height -90);
			this.setSize(wantedRoomFrameSize);
		}
		
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.decode("#066d95"));
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		formatter = NumberFormat.getCurrencyInstance();
		formatter.setCurrency(Currency.getInstance(Locale.getDefault()));
		
		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setAutoscrolls(true);
		panel.setPreferredSize(new Dimension(10, 55));
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(null);
		
		postingBtn = new JButton("Posting");
		postingBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						new PostingExternalWindow(roomText);
						populatePostPayTable(postPayModel);
					}
				});
			}
		});
		postingBtn.setAutoscrolls(true);
		postingBtn.setFont(new Font("Arial", Font.PLAIN, 15));
		postingBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
		postingBtn.setIcon(new ImageIcon(RoomExternalWindow.class.getResource("/com/coder/hms/icons/room_posting.png")));
		postingBtn.setBounds(10, 5, 125, 43);
		panel.add(postingBtn);
		
		paymentBtn = new JButton("Payment");
		paymentBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						new PaymentExternalWindow(roomText);
						populatePostPayTable(postPayModel);
					}
				});
			}
		});
		paymentBtn.setAutoscrolls(true);
		paymentBtn.setFont(new Font("Arial", Font.PLAIN, 15));
		paymentBtn.setIcon(new ImageIcon(RoomExternalWindow.class.getResource("/com/coder/hms/icons/payment_cash.png")));
		paymentBtn.setBounds(142, 5, 125, 43);
		panel.add(paymentBtn);
		
		checkoutBtn = new JButton("Checkout");
		checkoutBtn.setAutoscrolls(true);
		checkoutBtn.setFont(new Font("Arial", Font.PLAIN, 15));
		checkoutBtn.setIcon(new ImageIcon(RoomExternalWindow.class.getResource("/com/coder/hms/icons/room_checkout.png")));
		checkoutBtn.setBounds(274, 5, 125, 43);
		panel.add(checkoutBtn);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setSize(new Dimension(5, 20));
		verticalStrut.setMinimumSize(new Dimension(5, 20));
		verticalStrut.setIgnoreRepaint(true);
		verticalStrut.setPreferredSize(new Dimension(5, 20));
		verticalStrut.setBackground(Color.BLACK);
		verticalStrut.setBounds(406, 5, 10, 43);
		panel.add(verticalStrut);
		
		totalPriceField = new JFormattedTextField(formatter);
		totalPriceField.setAlignmentY(Component.TOP_ALIGNMENT);
		totalPriceField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		totalPriceField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		totalPriceField.setFont(new Font("Arial", Font.BOLD, 15));
		totalPriceField.setBackground(new Color(240, 128, 128));
		totalPriceField.setEditable(false);
		totalPriceField.setBounds(1024, 28, 86, 26);
		panel.add(totalPriceField);
		totalPriceField.setColumns(10);
		
		JLabel balanceLbl = new JLabel("Balance : ");
		balanceLbl.setAutoscrolls(true);
		balanceLbl.setAlignmentY(Component.TOP_ALIGNMENT);
		balanceLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
		balanceLbl.setFont(new Font("Arial", Font.BOLD, 13));
		balanceLbl.setHorizontalTextPosition(SwingConstants.CENTER);
		balanceLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		balanceLbl.setBounds(907, 5, 114, 20);
		panel.add(balanceLbl);
		
		balanceField = new JFormattedTextField(formatter);
		balanceField.setAlignmentY(Component.TOP_ALIGNMENT);
		balanceField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		balanceField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		balanceField.setFont(new Font("Arial", Font.BOLD, 15));
		balanceField.setBackground(new Color(102, 205, 170));
		balanceField.setEditable(false);
		balanceField.setBounds(1024, 1, 86, 26);
		panel.add(balanceField);
		balanceField.setColumns(10);
		
		JLabel totalLbl = new JLabel(" Total account : ");
		totalLbl.setAutoscrolls(true);
		totalLbl.setAlignmentY(Component.TOP_ALIGNMENT);
		totalLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
		totalLbl.setFont(new Font("Arial", Font.BOLD, 13));
		totalLbl.setHorizontalTextPosition(SwingConstants.CENTER);
		totalLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		totalLbl.setBounds(907, 30, 114, 20);
		panel.add(totalLbl);
		
		JPanel reservInfoHolder = new JPanel();
		reservInfoHolder.setAlignmentY(Component.TOP_ALIGNMENT);
		reservInfoHolder.setAlignmentX(Component.RIGHT_ALIGNMENT);
		reservInfoHolder.setAutoscrolls(true);
		reservInfoHolder.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		reservInfoHolder.setBackground(new Color(176, 196, 222));
		reservInfoHolder.setPreferredSize(new Dimension(220, 10));
		getContentPane().add(reservInfoHolder, BorderLayout.EAST);
		reservInfoHolder.setLayout(null);
		
		JLabel lblReservatonInfo = new JLabel("RESERVATION INFO");
		lblReservatonInfo.setFont(new Font("Verdana", Font.BOLD, 14));
		lblReservatonInfo.setHorizontalTextPosition(SwingConstants.CENTER);
		lblReservatonInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblReservatonInfo.setBounds(2, 4, 216, 29);
		reservInfoHolder.add(lblReservatonInfo);
		
		saveChangesBtn = new JButton("SAVE CHANGES");
		saveChangesBtn.setIcon(new ImageIcon(RoomExternalWindow.class.getResource("/com/coder/hms/icons/reserv_save.png")));
		saveChangesBtn.setAutoscrolls(true);
		saveChangesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null, "Are you sure to continue?", JOptionPane.OPTION_TYPE_PROPERTY, JOptionPane.YES_NO_OPTION);
				
				if(response == JOptionPane.YES_OPTION) {
					//Save everything
				}
				
				else {
					return;
				}
			}
		});
		saveChangesBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
		saveChangesBtn.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		saveChangesBtn.setBounds(2, 285, 218, 29);
		reservInfoHolder.add(saveChangesBtn);
		
		JLabel IdLbl = new JLabel("Id : ");
		IdLbl.setBounds(12, 42, 46, 14);
		reservInfoHolder.add(IdLbl);
		
		IdField = new JTextField();
		IdField.setEditable(false);
		IdField.setBounds(91, 36, 86, 20);
		reservInfoHolder.add(IdField);
		IdField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Checkin : ");
		lblNewLabel.setBounds(12, 205, 70, 14);
		reservInfoHolder.add(lblNewLabel);
		
		checkinDate = new JDateChooser();
		checkinDate.setEnabled(false);
		checkinDate.setDateFormatString("yyyy-MM-dd");
		checkinDate.setBounds(91, 202, 127, 20);
		reservInfoHolder.add(checkinDate);
		
		JLabel lblCheckoutDate = new JLabel("Checkout : ");
		lblCheckoutDate.setBounds(12, 233, 70, 14);
		reservInfoHolder.add(lblCheckoutDate);
		
		checkoutDate = new JDateChooser();
		checkoutDate.setDateFormatString("yyyy-MM-dd");
		checkoutDate.setBounds(91, 230, 127, 20);
		reservInfoHolder.add(checkoutDate);
		
		JLabel lblGroup = new JLabel("Group : ");
		lblGroup.setBounds(12, 65, 70, 14);
		reservInfoHolder.add(lblGroup);
		
		groupNameField = new JTextField();
		groupNameField.setEditable(false);
		groupNameField.setBounds(91, 62, 125, 20);
		reservInfoHolder.add(groupNameField);
		groupNameField.setColumns(10);
		
		JLabel lblAgency = new JLabel("Agency : ");
		lblAgency.setBounds(12, 93, 70, 14);
		reservInfoHolder.add(lblAgency);
		
		agencyField = new JTextField();
		agencyField.setEditable(false);
		agencyField.setBounds(91, 90, 125, 20);
		reservInfoHolder.add(agencyField);
		agencyField.setColumns(10);
		
		JLabel lblPrice = new JLabel("Price : ");
		lblPrice.setBounds(12, 121, 70, 14);
		reservInfoHolder.add(lblPrice);
		
		priceField = new JFormattedTextField();
		priceField.setEditable(false);
		priceField.setBounds(91, 118, 64, 20);
		reservInfoHolder.add(priceField);
		
		currencyField = new JTextField();
		currencyField.setEditable(false);
		currencyField.setBounds(156, 118, 61, 20);
		reservInfoHolder.add(currencyField);
		currencyField.setColumns(10);
		
		JLabel lblCreditType = new JLabel("Credit type : ");
		lblCreditType.setBounds(12, 150, 70, 14);
		reservInfoHolder.add(lblCreditType);
		
		creditField = new JTextField();
		creditField.setBounds(91, 146, 125, 20);
		reservInfoHolder.add(creditField);
		creditField.setColumns(10);
		
		JLabel lblHostType = new JLabel("Host type : ");
		lblHostType.setBounds(12, 177, 70, 14);
		reservInfoHolder.add(lblHostType);
		
		hostTypeField = new JTextField();
		hostTypeField.setBounds(91, 174, 127, 20);
		reservInfoHolder.add(hostTypeField);
		hostTypeField.setColumns(10);
		
		JLabel lblTotalDays = new JLabel("Total days : ");
		lblTotalDays.setBounds(12, 260, 70, 14);
		reservInfoHolder.add(lblTotalDays);
		
		totalDaysField = new JTextField();
		totalDaysField.setEditable(false);
		totalDaysField.setBounds(91, 257, 86, 20);
		reservInfoHolder.add(totalDaysField);
		totalDaysField.setColumns(10);
		
		JPanel cusomerTableHolder = new JPanel();
		cusomerTableHolder.setBackground(Color.decode("#066d95"));
		cusomerTableHolder.setAutoscrolls(true);
		getContentPane().add(cusomerTableHolder, BorderLayout.CENTER);
		cusomerTableHolder.setLayout(new BorderLayout(0, 0));
		
		roomNote = new JTextPane();
		roomNote.setLocale(new Locale("tr", "TR"));
		roomNote.setToolTipText("Write some note.");
		roomNote.setMargin(new Insets(5, 5, 5, 5));
		roomNote.setPreferredSize(new Dimension(0, 45));
		roomNote.setBackground(new Color(255, 255, 224));
		roomNote.setAlignmentX(Component.LEFT_ALIGNMENT);
		roomNote.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		roomNote.setFont(new Font("Arial", Font.BOLD, 15));
		roomNote.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		cusomerTableHolder.add(roomNote, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.decode("#e1fcff"));
		scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		cusomerTableHolder.add(scrollPane, BorderLayout.NORTH);
		
		
		populateCustomerTable(roomText, customerModel);
		
		customerTable = new JTable(customerModel);
		customerTable.setCellSelectionEnabled(false);
		customerTable.getTableHeader().setDefaultRenderer(THR);
		customerTable.addMouseListener(openCustomerListener());
		scrollPane.setViewportView(customerTable);
		
		JPanel postTableHolder = new JPanel();
		postTableHolder.setPreferredSize(new Dimension(10, 300));
		getContentPane().add(postTableHolder, BorderLayout.SOUTH);
		postTableHolder.setLayout(new BorderLayout(0, 0));
		
		JScrollPane postableScrollPane = new JScrollPane();
		postableScrollPane.setBackground(Color.decode("#e1fcff"));
		postableScrollPane.setBackground(new Color(230, 230, 250));
		postableScrollPane.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		postTableHolder.add(postableScrollPane, BorderLayout.CENTER);
		
		populatePostPayTable(postPayModel);
		
		payPostTable = new JTable(postPayModel);
		payPostTable.setDefaultRenderer(Object.class, payPostRenderer);
		payPostTable.setCellSelectionEnabled(false);
		payPostTable.getTableHeader().setDefaultRenderer(THR);
		postableScrollPane.setViewportView(payPostTable);
		
		
		populateReservationDetail();
		
		this.setVisible(true);
	}

	private void populateReservationDetail() {
		
		final RoomDaoImpl roomDaoImpl = new RoomDaoImpl();
		final Room theRoom = roomDaoImpl.getRoomByRoomNumber(roomNumber);
		final ReservationDaoImpl reservationDaoImpl = new ReservationDaoImpl();
		Reservation reservation = reservationDaoImpl.getReservationById(theRoom.getReservationId());
		
		IdField.setText(reservation.getId() + "");
		
		groupNameField.setText(reservation.getGroupName());
		
		agencyField.setText(reservation.getAgency());
		
		
		priceField.setValue(Float.parseFloat(theRoom.getPrice()));
		
		if(theRoom.getCurrency().equalsIgnoreCase("TURKISH LIRA")) {
			currencyField.setText("TL");
		}
		
		else {
			currencyField.setText(theRoom.getCurrency());
		}
		
		creditField.setText(reservation.getCreditType());
		
		hostTypeField.setText(reservation.getHostType());
		
		LocalDate localDate = LocalDate.parse(reservation.getCheckinDate());
		Date date = java.sql.Date.valueOf(localDate);
		checkinDate.setDate(date);
		
		localDate = LocalDate.parse(reservation.getCheckoutDate());
		date = java.sql.Date.valueOf(localDate);
		checkoutDate.setDate(date);
		
		totalDaysField.setText(reservation.getTotalDays() + "");
		
		final double totalPrice = Double.parseDouble(theRoom.getTotalPrice());
		totalPriceField.setValue(totalPrice);
		final double roombalance = Double.parseDouble(theRoom.getBalance());
		balanceField.setValue(roombalance);
	}
	
	public void populateCustomerTable(String roomText, DefaultTableModel model) {
	
		//clean table model
		model.setRowCount(0);
		
		//import all customers from database
		final RoomDaoImpl roomDaoImpl = new RoomDaoImpl();
		final Room foundedRoom = roomDaoImpl.getRoomByRoomNumber(roomText);
		
		final CustomerDaoImpl customerDaoImpl = new CustomerDaoImpl();
		final List<Customer> custmerList = customerDaoImpl.getCustomerByReservId(foundedRoom.getReservationId());
		
		int index = 0;
		//populate table model with loop
		for(Customer cst: custmerList) {
			index++;
			final Object[] rowData = new Object[]{index, cst.getFirstName(), cst.getLastName()};
			model.addRow(rowData);
		}
	}
	
	private MouseListener openCustomerListener() {
		final MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(e.getClickCount() == 2) {
//					final int rowIndex = customerTable.getSelectedRow();
//					final String name = customerTable.getValueAt(rowIndex, 1).toString();
//					final String lastname = customerTable.getValueAt(rowIndex, 2).toString();
					
					/* 1- Create Customer detail window and populate it.
					 * 2- Show customer if changed any detail saveOrUpdate it.
					*/
					final JDialog dialog = new JDialog();
					dialog.setAlwaysOnTop(true);
					JOptionPane.showMessageDialog(dialog, "PLEASE BE PATIENT\nRequested page is under development phase",
							JOptionPane.MESSAGE_PROPERTY, JOptionPane.INFORMATION_MESSAGE);
				}
				
				super.mousePressed(e);
			}
		};
		return adapter;
	}
	
	private void populatePostPayTable(DefaultTableModel model) {
		
		//import all customers from database
		final PostingDaoImpl postingDaoImpl = new PostingDaoImpl();
		List<Posting> postingList = postingDaoImpl.getAllPostingsByRoomNumber(roomNumber);
		
		final PaymentDaoImpl paymentDaoImpl = new PaymentDaoImpl();
		List<Payment> paymentlist = paymentDaoImpl.getAllPaymentsByRoomNumber(roomNumber);
		
		//clean table model
		model.setRowCount(0);
		
		for(Posting pos: postingList) {
			
			model.addRow(new Object[]{pos.getId(), pos.getPostType(), pos.getTitle(), pos.getPrice(),
					pos.getCurrency(), pos.getExplanation(), pos.getDateTime()});
		}
		
		for(Payment pay: paymentlist) {
			
			model.addRow(new Object[]{pay.getId(), pay.getPaymentType() ,pay.getTitle(), pay.getPrice(),
					pay.getCurrency(), pay.getExplanation(), pay.getDateTime()});
		}
		
		model.fireTableDataChanged();
	}
}






