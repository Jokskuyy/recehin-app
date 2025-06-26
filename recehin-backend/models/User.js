const db = require("../config/database");

class User {
  constructor(name, email, password, phone = null) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
  }

  // Simpan user baru
  async save() {
    const sql =
      "INSERT INTO users (name, email, password, phone) VALUES (?, ?, ?, ?)";
    const [result] = await db.execute(sql, [
      this.name,
      this.email,
      this.password,
      this.phone,
    ]);
    return result.insertId;
  }

  // Cari user berdasarkan email
  static async findByEmail(email) {
    const sql = "SELECT * FROM users WHERE email = ?";
    const [rows] = await db.execute(sql, [email]);
    return rows[0];
  }

  // Cari user berdasarkan ID
  static async findById(id) {
    const sql =
      "SELECT id, name, email, phone, created_at FROM users WHERE id = ?";
    const [rows] = await db.execute(sql, [id]);
    return rows[0];
  }

  // Cek apakah email sudah ada
  static async emailExists(email) {
    const sql = "SELECT id FROM users WHERE email = ?";
    const [rows] = await db.execute(sql, [email]);
    return rows.length > 0;
  }

  // Update password user
  static async updatePassword(userId, hashedPassword) {
    const sql = "UPDATE users SET password = ? WHERE id = ?";
    const [result] = await db.execute(sql, [hashedPassword, userId]);
    return result.affectedRows > 0;
  }
}

module.exports = User;
