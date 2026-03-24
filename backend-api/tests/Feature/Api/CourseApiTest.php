<?php

namespace Tests\Feature\Api;

use App\Models\Course;
use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Tests\TestCase;

class CourseApiTest extends TestCase
{
    use RefreshDatabase;

    public function test_can_list_courses()
    {
        $user = User::factory()->create(['role' => 'admin']);

        Course::factory()->create(['name' => 'BSCS']);
        Course::factory()->create(['name' => 'BSIT']);

        $response = $this->actingAs($user)
            ->getJson('/api/v1/courses');

        $response->assertStatus(200)
            ->assertJsonPath('status', 'success')
            ->assertJsonPath('message', 'Courses retrieved successfully')
            ->assertJsonCount(2, 'data')
            ->assertJsonPath('data.0.name', 'BSCS')
            ->assertJsonPath('data.1.name', 'BSIT');
    }

    public function test_unauthenticated_user_cannot_list_courses()
    {
        $response = $this->getJson('/api/v1/courses');

        $response->assertStatus(401);
    }
}
