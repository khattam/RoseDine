import 'package:flutter/material.dart';
import 'menu_item_screen.dart';

class BrunchScreen extends StatelessWidget {
  final DateTime selectedDate;

  const BrunchScreen({Key? key, required this.selectedDate}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MenuItemScreen(selectedDate: selectedDate, mealType: 'Brunch');
  }
}