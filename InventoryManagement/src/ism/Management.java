package ism;

import java.awt.EventQueue;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class Management extends JFrame {

	/**
	 * 
	 */
	
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtPrice;
	private JTable table;
	private Connection con;
	private JTextField txtId;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Management frame = new Management();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Management() {
		con=DbConnection.connect();
		if(con==null) {
			JOptionPane.showMessageDialog(Management.this, "Database failed to connect");
		}
		else {
			JOptionPane.showMessageDialog(Management.this, "Database connect successfully");
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 818, 582);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Products Name:");
		lblNewLabel.setBounds(10, 139, 141, 30);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(lblNewLabel);
		
		txtName = new JTextField();
		txtName.setBounds(160, 137, 239, 30);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Price:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(57, 194, 94, 19);
		contentPane.add(lblNewLabel_1);
		
		txtPrice = new JTextField();
		txtPrice.setBounds(160, 186, 239, 30);
		contentPane.add(txtPrice);
		txtPrice.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(687, 211, -355, 206);
		contentPane.add(scrollPane);
		
		
		table = new JTable();
		
		//fetching 
		table.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		ResultSet rs=fetchProducts();
		table.setModel(DbUtils.resultSetToTableModel(rs));
		scrollPane.setViewportView(table);
		
		
		table.setBounds(194, 236, 533, 249);
		contentPane.add(table);
		
		JButton btnAddProduct = new JButton("Add Product");
		btnAddProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name=txtName.getText();
				int price=Integer.parseInt(txtPrice.getText());
				int id=Integer.parseInt(txtId.getText());
				int rowAffected=addProduct(id,name,price);
				if(rowAffected > 0) {
					JOptionPane.showMessageDialog(Management.this, "Product added successfully");
					ResultSet rs = fetchProducts();
					table.setModel(DbUtils.resultSetToTableModel(rs));
				}
				else {
					JOptionPane.showMessageDialog(Management.this, "Product added unsuccessfully");
				}
			}
		});
		btnAddProduct.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAddProduct.setBounds(220, 495, 159, 40);
		contentPane.add(btnAddProduct);
		
		JButton btnDelete = new JButton("Delete Product");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				//delete operation
				int selectedRow =table.getSelectedRow();
				if(selectedRow == -1) {
					JOptionPane.showMessageDialog(Management.this, "please select the product");
					
				}
				else {
					int id=(int)table.getValueAt(selectedRow,0);
					int rowAffected=deleteProduct(id);
					if(rowAffected > 0) {
						JOptionPane.showMessageDialog(Management.this, "please deleted the product");
						ResultSet rs=fetchProducts();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						
					} else {
						JOptionPane.showMessageDialog(Management.this, "Fail");
					}
					
					
				}
				
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDelete.setBounds(433, 495, 162, 40);
		contentPane.add(btnDelete);
		
		JLabel lblNewLabel_2 = new JLabel("ID:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(57, 98, 94, 19);
		contentPane.add(lblNewLabel_2);
		
		txtId = new JTextField();
		txtId.setBounds(160, 87, 239, 30);
		contentPane.add(txtId);
		txtId.setColumns(10);
	}// end of constructor
	private ResultSet fetchProducts() {
		try {
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("SELECT * FROM management");
			return rs;
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	private int addProduct(int id,String name,int price) {
		try {
			PreparedStatement st=con.prepareStatement("INSERT INTO management(ID,Pname,price) VALUES(?,?,?)");
			st.setInt(1, id);
			st.setString(2, name);
			st.setInt(3, price);
			int rowsAffected=st.executeUpdate();
			return rowsAffected;
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	private int deleteProduct(int id) {
		try {
			Statement st=con.createStatement();
			String sq1="DELETE FROM management WHERE ID="+id;
			int rowsAffected=st.executeUpdate(sq1);
			return rowsAffected;
		}
		catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
