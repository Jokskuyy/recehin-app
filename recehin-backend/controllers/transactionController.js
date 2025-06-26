const Transaction = require("../models/Transaction");

class TransactionController {
  // Membuat transaksi baru
  static async createTransaction(req, res) {
    try {
      const { categoryId, amount, description, transactionDate } = req.body;
      const userId = req.user.id; // Diambil dari token JWT (authMiddleware)

      const transactionId = await Transaction.create(
        userId,
        categoryId,
        amount,
        description,
        transactionDate
      );

      res.status(201).json({
        success: true,
        message: "Transaksi berhasil ditambahkan",
        data: { id: transactionId },
      });
    } catch (error) {
      console.error("Create transaction error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }

  // Mendapatkan semua transaksi user
  static async getUserTransactions(req, res) {
    try {
      const userId = req.user.id;
      const transactions = await Transaction.findAllByUser(userId);

      res.json({
        success: true,
        data: transactions,
      });
    } catch (error) {
      console.error("Get transactions error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }

  // Menghapus transaksi
  static async deleteTransaction(req, res) {
    try {
      const { id } = req.params;
      const userId = req.user.id;

      const deleted = await Transaction.delete(id, userId);

      if (!deleted) {
        return res.status(404).json({
          success: false,
          message: "Transaksi tidak ditemukan atau Anda tidak punya hak akses.",
        });
      }

      res.json({ success: true, message: "Transaksi berhasil dihapus." });
    } catch (error) {
      console.error("Delete transaction error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }
}

module.exports = TransactionController;
