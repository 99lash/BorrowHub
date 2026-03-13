<?php

namespace App\Http\Requests\Students;

use Illuminate\Foundation\Http\FormRequest;

class UpdateStudentRequest extends FormRequest
{
    public function authorize(): bool
    {
        return true;
    }

    public function rules(): array
    {
        return [
            'student_number' => 'sometimes|required|string|unique:students,student_number,' . $this->route('student'),
            'name' => 'sometimes|required|string|max:255',
            'course_id' => 'sometimes|required|exists:courses,id'
        ];
    }
}
