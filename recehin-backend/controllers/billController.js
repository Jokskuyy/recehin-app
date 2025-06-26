const Bill = require("../models/Bill");

class BillController {
  // Membuat tagihan baru
  static async createBill(req, res) {
    try {
      // Mengambil data dari body request sesuai dengan perubahan database
      const { categoryId, name, amount, frequency, nextDueDate } = req.body;
      const userId = req.user.id;

      const billId = await Bill.create(
        userId,
        categoryId,
        name,
        amount,
        frequency,
        nextDueDate
      );

      res.status(201).json({
        success: true,
        message: "Tagihan rutin berhasil ditambahkan",
        data: { id: billId },
      });
    } catch (error) {
      console.error("Create bill error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }

  // Mendapatkan semua tagihan milik user
  static async getUserBills(req, res) {
    try {
      const userId = req.user.id;
      const bills = await Bill.findAllByUser(userId);

      res.json({
        success: true,
        data: bills,
      });
    } catch (error) {
      console.error("Get bills error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }

  // Menghapus tagihan (opsional, jika diperlukan nanti)
  static async deleteBill(req, res) {
    try {
      const { id } = req.params;
      const userId = req.user.id;
      const deleted = await Bill.delete(id, userId);

      if (!deleted) {
        return res
          .status(404)
          .json({ success: false, message: "Tagihan tidak ditemukan" });
      }

      res.json({ success: true, message: "Tagihan berhasil dihapus" });
    } catch (error) {
      console.error("Delete bill error:", error);
      res
        .status(500)
        .json({ success: false, message: "Terjadi kesalahan server" });
    }
  }
}

module.exports = BillController;
