<?php

namespace App\Http\Requests\Students;

use Illuminate\Foundation\Http\FormRequest;

class StoreStudentRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'student_number' => 'required|string|unique:students,student_number',
            'name' => 'required|string|max:255',
            'course_id' => 'required|exists:courses,id'
        ];
    }
}
