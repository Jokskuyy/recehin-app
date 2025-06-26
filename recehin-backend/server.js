// Mengimpor konfigurasi aplikasi dari file app.js
const app = require("./app");

// Mengambil port dari environment variable atau default ke 3000
const PORT = process.env.PORT || 3000;

// Menjalankan server dan mendengarkan koneksi di port yang ditentukan
app.listen(PORT, () => {
  console.log(`🚀 Recehin API server running on port ${PORT}`);
  console.log(`📍 Health check: http://localhost:${PORT}/api/health`);
  console.log(`🌐 Base URL: http://localhost:${PORT}`);
});
