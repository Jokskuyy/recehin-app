const express = require("express");
const { body } = require("express-validator");
const AuthController = require("../controllers/authController");
const authMiddleware = require("../middleware/auth");
const { handleValidationErrors } = require("../utils/validation");

const router = express.Router();
// Validation rules
const registerValidation = [
  body("name")
    .trim()
    .isLength({ min: 2, max: 100 })
    .withMessage("Nama harus antara 2-100 karakter"),
  body("email").isEmail().normalizeEmail().withMessage("Email tidak valid"),
  body("password")
    .isLength({ min: 6 })
    .withMessage("Password minimal 6 karakter"),
  body("phone")
    .optional()
    .isMobilePhone("id-ID")
    .withMessage("Nomor telepon tidak valid"),
];

const loginValidation = [
  body("email").isEmail().normalizeEmail().withMessage("Email tidak valid"),
  body("password").notEmpty().withMessage("Password wajib diisi"),
];

const forgotPasswordValidation = [
  body("email").isEmail().normalizeEmail().withMessage("Email tidak valid"),
];

const resetPasswordValidation = [
  body("token").notEmpty().withMessage("Token wajib diisi"),
  body("newPassword")
    .isLength({ min: 6 })
    .withMessage("Password baru minimal 6 karakter"),
];

// Routes
router.post(
  "/register",
  registerValidation,
  handleValidationErrors,
  AuthController.register
);
router.post(
  "/login",
  loginValidation,
  handleValidationErrors,
  AuthController.login
);
router.get("/profile", authMiddleware, AuthController.getProfile);
router.post(
  "/forgot-password",
  forgotPasswordValidation,
  handleValidationErrors,
  AuthController.forgotPassword
);
router.post(
  "/reset-password",
  resetPasswordValidation,
  handleValidationErrors,
  AuthController.resetPassword
);

module.exports = router;
