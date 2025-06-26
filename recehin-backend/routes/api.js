const express = require("express");
const { body } = require("express-validator");
const authMiddleware = require("../middleware/auth");
const { handleValidationErrors } = require("../utils/validation");
const CategoryController = require("../controllers/categoryController");
const TransactionController = require("../controllers/transactionController");
const BillController = require("../controllers/billController");

const router = express.Router();

// --- Routes Kategori (DIUBAH) ---
const categoryValidation = [
  body("name").isString().notEmpty().withMessage("Nama kategori wajib diisi"),
  body("type")
    .isIn(["income", "expense"])
    .withMessage("Tipe harus 'income' atau 'expense'"),
];
router.get("/categories", authMiddleware, CategoryController.getUserCategories);
router.post(
  "/categories",
  authMiddleware,
  categoryValidation,
  handleValidationErrors,
  CategoryController.createCategory
);
router.put(
  "/categories/:id",
  authMiddleware,
  categoryValidation,
  handleValidationErrors,
  CategoryController.updateCategory
);
router.delete(
  "/categories/:id",
  authMiddleware,
  CategoryController.deleteCategory
);

// --- Routes Transaksi (Tidak Berubah) ---
// Catatan: 'description' dan 'transactionDate' sudah ada di sini sejak awal.
const transactionValidation = [
  body("categoryId").isInt().withMessage("Kategori ID harus berupa angka"),
  body("amount")
    .isDecimal({ decimal_digits: "1,2" })
    .withMessage("Jumlah harus berupa angka desimal"),
  body("description").optional().isString(),
  body("transactionDate")
    .isISO8601()
    .toDate()
    .withMessage("Format tanggal transaksi tidak valid (YYYY-MM-DD)"),
];
router.post(
  "/transactions",
  authMiddleware,
  transactionValidation,
  handleValidationErrors,
  TransactionController.createTransaction
);
router.get(
  "/transactions",
  authMiddleware,
  TransactionController.getUserTransactions
);
router.delete(
  "/transactions/:id",
  authMiddleware,
  TransactionController.deleteTransaction
);

// --- Routes Tagihan (DIUBAH) ---
const billValidation = [
  body("categoryId").isInt().withMessage("Kategori ID harus berupa angka"),
  body("name").isString().notEmpty().withMessage("Nama tagihan wajib diisi"),
  body("amount")
    .isDecimal({ decimal_digits: "1,2" })
    .withMessage("Jumlah harus berupa angka desimal"),
  // DIUBAH: Validasi untuk field baru
  body("frequency")
    .isIn(["harian", "mingguan", "bulanan"])
    .withMessage("Frekuensi tidak valid"),
  body("nextDueDate")
    .isISO8601()
    .toDate()
    .withMessage("Format tanggal jatuh tempo tidak valid (YYYY-MM-DD)"),
];
router.post(
  "/bills",
  authMiddleware,
  billValidation,
  handleValidationErrors,
  BillController.createBill
);
router.get("/bills", authMiddleware, BillController.getUserBills);

module.exports = router;
