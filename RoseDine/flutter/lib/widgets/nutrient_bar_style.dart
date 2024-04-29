import 'package:flutter/material.dart';

class NutrientBarStyle {
  final TextStyle labelStyle;
  final TextStyle valueStyle;

  const NutrientBarStyle({
    this.labelStyle = const TextStyle(
      fontSize: 16,
      fontWeight: FontWeight.normal,
    ),
    this.valueStyle = const TextStyle(
      fontSize: 16,
      fontWeight: FontWeight.normal,
    ),
  });

  NutrientBarStyle copyWith({
    TextStyle? labelStyle,
    TextStyle? valueStyle,
  }) {
    return NutrientBarStyle(
      labelStyle: labelStyle ?? this.labelStyle,
      valueStyle: valueStyle ?? this.valueStyle,
    );
  }
}