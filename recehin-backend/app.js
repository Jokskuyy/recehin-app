const express = require("express");
const cors = require("cors");
require("dotenv").config();

// Import routes
const authRoutes = require("./routes/auth");
const apiRoutes = require("./routes/api");

const app = express();

// Middleware
app.use(cors());
app.use(express.json({ limit: "10mb" }));
app.use(express.urlencoded({ extended: true }));

// --- Pendaftaran Rute Utama ---
// Semua rute yang berhubungan dengan autentikasi akan memiliki awalan /api/auth
app.use("/api/auth", authRoutes);
// Semua rute lain (transaksi, kategori, dll) akan memiliki awalan /api
app.use("/api", apiRoutes);

// --- Rute Tambahan ---
// Health check route untuk memastikan server hidup
app.get("/api/health", (req, res) => {
  res.status(200).json({
    success: true,
    message: "Recehin API is running",
    timestamp: new Date().toISOString(),
  });
});

// --- Error Handlers (HARUS DI BAGIAN PALING BAWAH) ---
// 404 handler untuk rute yang tidak ditemukan
app.use("*", (req, res) => {
  res.status(404).json({
    success: false,
    message: `Route ${req.originalUrl} not found`,
    method: req.method,
  });
});

// Global error handler untuk menangkap semua error server
app.use((err, req, res, next) => {
  console.error("UNHANDLED ERROR:", err.stack);
  res.status(500).json({
    success: false,
    message: "Internal Server Error",
  });
});

// Ekspor modul app agar bisa digunakan oleh server.js
module.exports = app;
