import 'package:flutter/material.dart';
import 'nutrient_bar.dart';
import 'nutrient_bar_style.dart';

class ExpandedContent extends StatelessWidget {
  final Map<String, dynamic> menuItem;

  const ExpandedContent({Key? key, required this.menuItem}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    const customStyle = NutrientBarStyle(
      labelStyle: TextStyle(
        fontSize: 18,
        fontWeight: FontWeight.bold,
      ),
      valueStyle: TextStyle(
        fontSize: 18,
        fontWeight: FontWeight.bold,
      ),
    );

    return Column(
      children: [
        NutrientBar(
          label: 'Calories',
          value: menuItem['calories'],
          maxValue: 1000,
          style: customStyle,
        ),
        SizedBox(height: 8),
        NutrientBar(
          label: 'Protein',
          value: menuItem['protein'],
          maxValue: 100,
          style: customStyle,
        ),
        SizedBox(height: 8),
        NutrientBar(
          label: 'Fats',
          value: menuItem['fats'],
          maxValue: 50,
          style: customStyle,
        ),
        SizedBox(height: 8),
        NutrientBar(
          label: 'Carbs',
          value: menuItem['carbs'],
          maxValue: 100,
          style: customStyle,
        ),
        SizedBox(height: 16),
        NutrientBar(
          label: 'Overall Rating',
          value: menuItem['overallStars'],
          maxValue: 5,
          barColor: Colors.yellow.shade700,
          style: customStyle,
        ),
      ],
    );
  }
}