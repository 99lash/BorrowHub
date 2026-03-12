<?php

namespace Database\Factories;

use App\Models\Category;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\Item>
 */
class ItemFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        $total = fake()->numberBetween(5, 20);
        return [
            'category_id' => Category::factory(),
            'name' => fake()->words(2, true),
            'total_quantity' => $total,
            'available_quantity' => $total,
            'status' => 'active',
        ];
    }
}
