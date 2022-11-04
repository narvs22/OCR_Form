<!DOCTYPE html>
<html lang="en">
<head>
	<!-- Setting the pages character encoding -->
	<meta charset="UTF-8">

	<!-- The meta viewport will scale my content to any device width -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- Link to my stylesheet -->
	<link rel="stylesheet" href="styles.css">
	<title>Finished</title>
</head>
<body>

	<h2>Applicants Data 2022</h2>

	<!-- Lets start with the table element -->
	<table>
		<!-- The first row is the tables header -->
		<tr>
			<th>First Name</th>
			<th>Surname</th>
			<th>DOB</th>
			<th>Address</th>
			<th>Contact</th>
			<th>Cause of Condition</th>
			<th>Insurance Company</th>
		</tr>

		<!-- Next we will have the template which we will use inside
	  		the loop and poplulate with the data from the json file. -->

		<?php

			$json_data = file_get_contents("FormSubmission.json");
			$forms = json_decode($json_data, true);
			if(count($forms) != 0){
				foreach ($forms as $form) {
					?>
						<tr>
							<td>
								<?php echo $form['First Name']; ?>
							</td>
							<td>
								<?php echo $form['Surname']; ?>
							</td>
							<td>
								<?php echo $form['DOB']; ?>
							</td>
							<td>
								<?php echo $form['Address']; ?>
							</td>
							<td>
								<?php echo $form['Contact Number']; ?>
							</td>
							<td>
								<?php echo $form['Cause of Condition']; ?>
							</td>
							<td>
								<?php echo $form['Insurance Company']; ?>
							</td>
						</tr>
					<?php
				}
			}
		?>
	</table>
</body>
</html>
