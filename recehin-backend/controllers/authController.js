const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const User = require("../models/User");
const Category = require("../models/Category");

class AuthController {
  // Register user baru
  static async register(req, res) {
    try {
      const { name, email, password, phone } = req.body;

      const emailExists = await User.emailExists(email);
      if (emailExists) {
        return res.status(400).json({
          success: false,
          message: "Email sudah terdaftar",
        });
      }

      const saltRounds = 12;
      const hashedPassword = await bcrypt.hash(password, saltRounds);

      const user = new User(name, email, hashedPassword, phone);
      const userId = await user.save();

      // Panggil fungsi untuk menyalin kategori default
      await Category.copyDefaultsForUser(userId);

      const token = jwt.sign({ id: userId, email }, process.env.JWT_SECRET, {
        expiresIn: process.env.JWT_EXPIRE,
      });

      res.status(201).json({
        success: true,
        message: "Registrasi berhasil",
        data: {
          user: {
            id: userId,
            name,
            email,
            phone,
          },
          token,
        },
      });
    } catch (error) {
      console.error("Register error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }

  // Login user
  static async login(req, res) {
    try {
      const { email, password } = req.body;

      const user = await User.findByEmail(email);
      if (!user) {
        return res.status(400).json({
          success: false,
          message: "Email atau password salah",
        });
      }

      const isPasswordValid = await bcrypt.compare(password, user.password);
      if (!isPasswordValid) {
        return res.status(400).json({
          success: false,
          message: "Email atau password salah",
        });
      }

      const token = jwt.sign(
        { id: user.id, email: user.email },
        process.env.JWT_SECRET,
        { expiresIn: process.env.JWT_EXPIRE }
      );

      res.json({
        success: true,
        message: "Login berhasil",
        data: {
          user: {
            id: user.id,
            name: user.name,
            email: user.email,
            phone: user.phone,
          },
          token,
        },
      });
    } catch (error) {
      console.error("Login error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }

  // Get user profile
  static async getProfile(req, res) {
    try {
      const user = await User.findById(req.user.id);
      res.json({
        success: true,
        data: {
          user: user,
        },
      });
    } catch (error) {
      console.error("Get profile error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }

  // Lupa password - generate reset token
  static async forgotPassword(req, res) {
    try {
      const { email } = req.body;

      const user = await User.findByEmail(email);
      if (!user) {
        return res.status(404).json({
          success: false,
          message: "Email tidak ditemukan",
        });
      }

      // Dalam implementasi nyata, token ini harus disimpan di DB dan dikirim via email
      const resetToken = jwt.sign(
        { id: user.id, purpose: "password_reset" },
        process.env.JWT_SECRET,
        { expiresIn: "1h" }
      );

      res.json({
        success: true,
        message:
          "Instruksi reset password telah dikirim (cek konsol untuk token)",
        data: {
          // Token ini ditampilkan hanya untuk tujuan development.
          // JANGAN kirim token ke response di lingkungan produksi.
          resetToken,
        },
      });
    } catch (error) {
      console.error("Forgot password error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }

  // Reset password menggunakan token
  static async resetPassword(req, res) {
    try {
      const { token, newPassword } = req.body;

      // Verifikasi reset token
      const decoded = jwt.verify(token, process.env.JWT_SECRET);
      if (decoded.purpose !== "password_reset") {
        return res.status(400).json({
          success: false,
          message: "Token tidak valid atau tujuan salah",
        });
      }

      // Hash password baru
      const saltRounds = 12;
      const hashedPassword = await bcrypt.hash(newPassword, saltRounds);

      // Update password di database
      const updated = await User.updatePassword(decoded.id, hashedPassword);
      if (!updated) {
        return res.status(400).json({
          success: false,
          message: "Gagal mengupdate password, user tidak ditemukan",
        });
      }

      res.json({
        success: true,
        message: "Password berhasil direset",
      });
    } catch (error) {
      if (error instanceof jwt.JsonWebTokenError) {
        return res.status(400).json({
          success: false,
          message: "Token tidak valid atau kadaluwarsa.",
        });
      }
      console.error("Reset password error:", error);
      res.status(500).json({
        success: false,
        message: "Terjadi kesalahan server",
      });
    }
  }
}

module.exports = AuthController;
