const db = require("../config/database");

class Transaction {
  // Buat transaksi baru
  static async create(
    userId,
    categoryId,
    amount,
    description,
    transactionDate
  ) {
    const sql = `
      INSERT INTO transactions (user_id, category_id, amount, description, transaction_date) 
      VALUES (?, ?, ?, ?, ?)`;
    const [result] = await db.execute(sql, [
      userId,
      categoryId,
      amount,
      description,
      transactionDate,
    ]);
    return result.insertId;
  }

  // Ambil semua transaksi milik seorang user
  static async findAllByUser(userId) {
    const sql = `
      SELECT t.id, t.amount, t.description, t.transaction_date, c.name as category_name, c.type as category_type
      FROM transactions t
      JOIN categories c ON t.category_id = c.id
      WHERE t.user_id = ?
      ORDER BY t.transaction_date DESC, t.created_at DESC`;
    const [rows] = await db.execute(sql, [userId]);
    return rows;
  }

  // Ambil satu transaksi berdasarkan ID dan User ID (untuk keamanan)
  static async findById(id, userId) {
    const sql = `
      SELECT t.id, t.amount, t.description, t.transaction_date, t.category_id, c.name as category_name, c.type as category_type
      FROM transactions t
      JOIN categories c ON t.category_id = c.id
      WHERE t.id = ? AND t.user_id = ?`;
    const [rows] = await db.execute(sql, [id, userId]);
    return rows[0];
  }

  // Hapus transaksi
  static async delete(id, userId) {
    const sql = "DELETE FROM transactions WHERE id = ? AND user_id = ?";
    const [result] = await db.execute(sql, [id, userId]);
    return result.affectedRows > 0;
  }
}

module.exports = Transaction;
