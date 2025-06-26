const db = require("../config/database");

class Category {
  // Membuat kategori baru untuk pengguna tertentu
  static async create(name, type, userId) {
    const sql = "INSERT INTO categories (name, type, user_id) VALUES (?, ?, ?)";
    const [result] = await db.execute(sql, [name, type, userId]);
    return result.insertId;
  }

  // Menyalin kategori default (global) untuk pengguna yang baru mendaftar
  static async copyDefaultsForUser(userId) {
    const copySql = `
      INSERT INTO categories (name, type, user_id)
      SELECT name, type, ? FROM categories WHERE user_id IS NULL
    `;
    await db.execute(copySql, [userId]);
  }

  // Menemukan semua kategori milik pengguna (termasuk yang default)
  static async findAllByUser(userId) {
    // Mengambil kategori milik user DAN kategori default (global)
    const sql =
      "SELECT * FROM categories WHERE user_id = ? OR user_id IS NULL ORDER BY type, name";
    const [rows] = await db.execute(sql, [userId]);
    return rows;
  }

  // Menemukan kategori berdasarkan ID dan memastikan itu milik user
  static async findById(id, userId) {
    const sql = "SELECT * FROM categories WHERE id = ? AND user_id = ?";
    const [rows] = await db.execute(sql, [id, userId]);
    return rows[0];
  }

  // Update kategori
  static async update(id, name, type, userId) {
    const sql =
      "UPDATE categories SET name = ?, type = ? WHERE id = ? AND user_id = ?";
    const [result] = await db.execute(sql, [name, type, id, userId]);
    return result.affectedRows > 0;
  }

  // Hapus kategori
  static async delete(id, userId) {
    const sql = "DELETE FROM categories WHERE id = ? AND user_id = ?";
    const [result] = await db.execute(sql, [id, userId]);
    return result.affectedRows > 0;
  }
}

module.exports = Category;
