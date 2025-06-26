const db = require("../config/database");

class Bill {
  // DIUBAH: Menyesuaikan dengan kolom frequency dan next_due_date
  static async create(
    userId,
    categoryId,
    name,
    amount,
    frequency,
    nextDueDate
  ) {
    const sql = `
      INSERT INTO bills (user_id, category_id, name, amount, frequency, next_due_date) 
      VALUES (?, ?, ?, ?, ?, ?)`;
    const [result] = await db.execute(sql, [
      userId,
      categoryId,
      name,
      amount,
      frequency,
      nextDueDate,
    ]);
    return result.insertId;
  }

  // DIUBAH: Mengambil kolom baru
  static async findAllByUser(userId) {
    const sql = `
      SELECT b.id, b.name, b.amount, b.frequency, b.next_due_date, b.is_active, c.name as category_name
      FROM bills b
      JOIN categories c ON b.category_id = c.id
      WHERE (c.user_id = ? OR c.user_id IS NULL) AND b.user_id = ?
      ORDER BY b.next_due_date ASC`;
    const [rows] = await db.execute(sql, [userId, userId]);
    return rows;
  }

  // Tidak ada perubahan
  static async delete(id, userId) {
    const sql = "DELETE FROM bills WHERE id = ? AND user_id = ?";
    const [result] = await db.execute(sql, [id, userId]);
    return result.affectedRows > 0;
  }
}

module.exports = Bill;
