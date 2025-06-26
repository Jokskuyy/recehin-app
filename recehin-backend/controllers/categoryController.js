const Category = require("../models/Category");

class CategoryController {
  // BARU: Membuat kategori baru
  static async createCategory(req, res) {
    try {
      const { name, type } = req.body;
      const userId = req.user.id;
      const categoryId = await Category.create(name, type, userId);
      res.status(201).json({
        success: true,
        message: "Kategori berhasil dibuat",
        data: { id: categoryId, name, type },
      });
    } catch (error) {
      console.error("Create category error:", error);
      res
        .status(500)
        .json({ success: false, message: "Terjadi kesalahan server" });
    }
  }

  // DIUBAH: Mendapatkan kategori milik user
  static async getUserCategories(req, res) {
    try {
      const userId = req.user.id;
      const categories = await Category.findAllByUser(userId);
      res.json({ success: true, data: categories });
    } catch (error) {
      console.error("Get categories error:", error);
      res
        .status(500)
        .json({ success: false, message: "Terjadi kesalahan server" });
    }
  }

  // BARU: Update kategori
  static async updateCategory(req, res) {
    try {
      const { id } = req.params;
      const { name, type } = req.body;
      const userId = req.user.id;
      const updated = await Category.update(id, name, type, userId);
      if (!updated) {
        return res
          .status(404)
          .json({ success: false, message: "Kategori tidak ditemukan" });
      }
      res.json({ success: true, message: "Kategori berhasil diperbarui" });
    } catch (error) {
      console.error("Update category error:", error);
      res
        .status(500)
        .json({ success: false, message: "Terjadi kesalahan server" });
    }
  }

  // BARU: Hapus kategori
  static async deleteCategory(req, res) {
    try {
      const { id } = req.params;
      const userId = req.user.id;
      const deleted = await Category.delete(id, userId);
      if (!deleted) {
        return res
          .status(404)
          .json({ success: false, message: "Kategori tidak ditemukan" });
      }
      res.json({ success: true, message: "Kategori berhasil dihapus" });
    } catch (error) {
      console.error("Delete category error:", error);
      res
        .status(500)
        .json({ success: false, message: "Terjadi kesalahan server" });
    }
  }
}

module.exports = CategoryController;
