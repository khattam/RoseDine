import 'package:flutter/material.dart';
import 'nutrient_bar_style.dart';

class NutrientBar extends StatelessWidget {
  final String label;
  final int value;
  final int maxValue;
  final Color barColor;
  final NutrientBarStyle style;

  const NutrientBar({
    Key? key,
    required this.label,
    required this.value,
    required this.maxValue,
    this.barColor = Colors.blue,
    this.style = const NutrientBarStyle(),
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final percentage = value / maxValue;
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 4),
      child: Row(
        children: [
          Expanded(
            flex: 2,
            child: Text('$label: ', style: style.labelStyle),
          ),
          Expanded(
            flex: 7,
            child: Container(
              height: 20,
              decoration: BoxDecoration(
                color: Colors.grey[300],
                borderRadius: BorderRadius.circular(10),
              ),
              child: Row(
                children: [
                  Flexible(
                    child: FractionallySizedBox(
                      widthFactor: percentage,
                      child: Container(
                        decoration: BoxDecoration(
                          color: barColor,
                          borderRadius: BorderRadius.circular(10),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
          Expanded(
            flex: 1,
            child: Text(
              '$value',
              textAlign: TextAlign.right,
              style: style.valueStyle,
            ),
          ),
        ],
      ),
    );
  }
}