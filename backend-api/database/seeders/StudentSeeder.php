<?php

namespace Database\Seeders;

use App\Models\Course;
use App\Models\Student;
use Illuminate\Database\Seeder;

class StudentSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $courses = Course::all();

        if ($courses->isEmpty()) {
            $this->call(CourseSeeder::class);
            $courses = Course::all();
        }

        $studentData = [
            ['id' => '20230733-N', 'last' => 'ABAN', 'first' => 'MARYNELLE', 'middle' => 'TESORO'],
            ['id' => '20230335-S', 'last' => 'ADRIANO', 'first' => 'ALWYN', 'middle' => 'SISON'],
            ['id' => '20230498-S', 'last' => 'AGUILAR', 'first' => 'MARK JUSTIN', 'middle' => 'BANSAG'],
            ['id' => '20230183-S', 'last' => 'ALCALDE', 'first' => 'LANCE KERWIN', 'middle' => 'DUMOL'],
            ['id' => '20231071-S', 'last' => 'ANGCO', 'first' => 'MARK CREED', 'middle' => 'PIANO'],
            ['id' => '20230427-S', 'last' => 'ANTONIO', 'first' => 'KEITH', 'middle' => 'MARQUEZ'],
            ['id' => '20230374-S', 'last' => 'BARCOS', 'first' => 'CHRISTIAN JAY', 'middle' => 'PADRIQUE'],
            ['id' => '20230388-S', 'last' => 'BLANCADA', 'first' => 'JEBREIL JHON', 'middle' => 'SOLIS'],
            ['id' => '20230114-S', 'last' => 'COLMO', 'first' => 'JOSHUA PAUL', 'middle' => 'MEDINA'],
            ['id' => '20231315-S', 'last' => 'CORTES', 'first' => 'ANGELA IZY', 'middle' => 'CAGUIMBAGA'],
            ['id' => '20191439-S', 'last' => 'CRUZ', 'first' => 'MARC ROGEL', 'middle' => 'R'],
            ['id' => '20230583-S', 'last' => 'DANTES', 'first' => 'JOYCE', 'middle' => 'MAITIM'],
            ['id' => '20231282-S', 'last' => 'DELA CRUZ', 'first' => 'JOACKYN CAYLE', 'middle' => 'BELLO'],
            ['id' => '20230474-S', 'last' => 'DOMDOM', 'first' => 'KENNETH', 'middle' => 'REYES'],
            ['id' => '20230372-S', 'last' => 'ESQUERRA', 'first' => 'JOVILYN', 'middle' => 'FLORES'],
            ['id' => '20231311-S', 'last' => 'GAMUL', 'first' => 'GLENN JIM', 'middle' => 'GALOS'],
            ['id' => '20230166-S', 'last' => 'GATMAITAN', 'first' => 'RONAN ALECK', 'middle' => 'ACAB'],
            ['id' => '20230653-S', 'last' => 'GEROLIA', 'first' => 'BRYAN JAMES', 'middle' => 'ALCANTARA'],
            ['id' => '20231211-S', 'last' => 'GERONIMO', 'first' => 'RENZ', 'middle' => 'CELADA'],
            ['id' => '20230138-S', 'last' => 'LEONARDO', 'first' => 'RAFAEL DOMINIC', 'middle' => '.'],
            ['id' => '20230216-S', 'last' => 'LOPEZ', 'first' => 'TRISHA MAE', 'middle' => 'TANGILE'],
            ['id' => '20231193-S', 'last' => 'MADRIAGA', 'first' => 'KYLE VINCENT', 'middle' => 'MANAHAN'],
            ['id' => '20230184-S', 'last' => 'MANIT', 'first' => 'JOHN ASHER', 'middle' => 'MELLIZA'],
            ['id' => '20230054-S', 'last' => 'OFIANGGA', 'first' => 'JOHN GABRIELLE', 'middle' => 'DELMUNDO'],
            ['id' => '20230295-S', 'last' => 'PANCO', 'first' => 'JOREJJ GEIO', 'middle' => 'SUCGANG'],
            ['id' => '20230685-S', 'last' => 'PARAFINA', 'first' => 'REX', 'middle' => 'CABILES'],
            ['id' => '20231329-S', 'last' => 'RILI', 'first' => 'ALBERTO MAGNO', 'middle' => 'CONSUELO'],
            ['id' => '20230646-S', 'last' => 'ROBLES', 'first' => 'RAIN LOUIE', 'middle' => '.'],
            ['id' => '20231314-S', 'last' => 'SUNGA JR', 'first' => 'EDGARDO', 'middle' => 'DOCTOLERO'],
        ];

        foreach ($studentData as $data) {
            $middle = $data['middle'] === '.' ? '' : ' ' . $data['middle'];
            $fullName = "{$data['first']}{$middle} {$data['last']}";

            Student::updateOrCreate(
                ['student_number' => $data['id']],
                [
                    'name' => $fullName,
                    'course_id' => $courses->random()->id,
                ]
            );
        }
    }
}
