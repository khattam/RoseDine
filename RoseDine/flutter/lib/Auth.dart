import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:email_validator/email_validator.dart';
import 'ScheduleScreen.dart';  // Import the next screen

class AuthScreen extends StatefulWidget {
  const AuthScreen({super.key});

  @override
  _AuthScreenState createState() => _AuthScreenState();
}

class _AuthScreenState extends State<AuthScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  String? _emailValidator(String? email) {
    if (email == null || !EmailValidator.validate(email)) {
      return 'Please enter a valid email address';
    }

    if (!email.endsWith('@rose-hulman.edu')) {
      return 'Invalid domain, use a @rose-hulman.edu email';
    }

    return null;
  }

  String? _passwordValidator(String? password) {
    if (password == null || password.length < 6) {
      return 'Password must be at least 6 characters long';
    }
    return null;
  }

  void _register() {
    if (_formKey.currentState!.validate()) {
      // Handle registration logic here
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Registration successful!')),
      );
    }
  }

  void _login() {
    if (_formKey.currentState!.validate()) {
      // Handle login logic here
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Login successful!')),
      );
    }
  }

  void _bypass() {
    // Navigate to the ScheduleScreen or the desired next screen
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => ScheduleScreen()),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Authentication')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // Spacing to improve visual separation
              SizedBox(height: 20),
              Text(
                'Welcome to RoseDine',
                style: Theme.of(context).textTheme.headline5,
                textAlign: TextAlign.center,
              ),
              SizedBox(height: 40),
              TextFormField(
                controller: _emailController,
                decoration: InputDecoration(
                  labelText: 'Email',
                  border: OutlineInputBorder(),
                ),
                validator: _emailValidator,
                keyboardType: TextInputType.emailAddress,
                inputFormatters: [
                  FilteringTextInputFormatter.deny(RegExp(r'\s')),
                ],
              ),
              SizedBox(height: 20),  // Add spacing between elements
              TextFormField(
                controller: _passwordController,
                decoration: InputDecoration(
                  labelText: 'Password',
                  border: OutlineInputBorder(),
                ),
                validator: _passwordValidator,
                obscureText: true,
              ),
              SizedBox(height: 30),  // More spacing
              ElevatedButton(
                onPressed: _register,
                child: const Text('Register'),
              ),
              SizedBox(height: 10),
              ElevatedButton(
                onPressed: _login,
                child: const Text('Log In'),
              ),
              // Add a new button for bypassing
              SizedBox(height: 10),  // Keep consistent spacing
              ElevatedButton(
                onPressed: _bypass,
                child: const Text('Bypass'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
